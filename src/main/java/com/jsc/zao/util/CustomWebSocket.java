package com.jsc.zao.util;

import com.jsc.zao.bean.Number;
import com.jsc.zao.bean.Target;
import com.jsc.zao.service.NotificationService;
import com.jsc.zao.service.NumberService;
import com.jsc.zao.service.ScheduledService;
import com.jsc.zao.service.TargetService;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ServerEndpoint 注解是一个类层次的注解，它的功能是将目前的类定义成一个websocket服务器端，
 * 注解的值将用于监听用户连接的终端访问的url地址，客户端可以通过这个url来连接到websocket的服务器端
 */
@ServerEndpoint(value = "/websocket/{userno}")
@Component
public class CustomWebSocket {
    Logger log= Logger.getLogger(CustomWebSocket.class);
    private static ScheduledService scheduledService;
    private String userno ="";

    @Autowired
    public void setScheduledService(ScheduledService scheduledService) {
        CustomWebSocket.scheduledService = scheduledService;
    }

    private static NotificationService notificationService;

    @Autowired
    public void setNotificationService(NotificationService notificationService) {
        CustomWebSocket.notificationService = notificationService;
    }


    private static TargetService targetService;

    @Autowired
    public void setTargetService(TargetService targetService) {
        CustomWebSocket.targetService = targetService;
    }

    private static NumberService numberService;

    @Autowired
    public void setNumberService(NumberService numberService) {
        CustomWebSocket.numberService = numberService;
    }

    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static int onlineCount = 0;
    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的CumWebSocket对象。
     * 若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
     */
    private static ConcurrentHashMap<String, CustomWebSocket> webSocketSet = new ConcurrentHashMap<String, CustomWebSocket>();

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;


    /**
     * 连接建立成功调用的方法
     *
     * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(@PathParam(value = "userno") String param, Session session, EndpointConfig config) {

        log.info("------- Initializing ----------------------");
        log.error("这是error-------------------------");
        log.debug("-------------------debug----------");
        log.trace("trace-----------");
        log.warn("====================warn=======================");
        log.info(param);
        userno = param;//接收到发送消息的人员编号
        this.session = session;
        webSocketSet.put(param, this);//加入map
        //添加在线人数
        addOnlineCount();
        scheduledService.sendOnline();
        System.out.println("新连接接入。当前在线人数为：" + getOnlineCount());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if (!userno.equals("")) {
            webSocketSet.remove(userno);  //从set中删除
            subOnlineCount();           //在线数减1
            log.info("有一连接关闭！当前在线人数为" + getOnlineCount());
        }
    }

    /**
     * 收到客户端消息后调用
     *
     * @param message
     * @param session
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        log.info("客户端发送的消息：" + message);
        log.info(message);
        JSONObject jsonObject = new JSONObject(message);

        //json ==> map
        Map<String, Object> map = jsonObject.toMap();
        String isTarget = (String) map.get("isTarget");
        map.put("isTarget", isTarget);
        Timestamp catchTime = DateConverter.strToSqlDate((String) map.get("catchTime"), "yyyy-MM-dd HH:mm:ss");
        System.out.println(catchTime);
        map.put("catchTime", catchTime);

        String imsi = (String) map.get("imsi");
        String imei = (String) map.get("imei");
        String isdn = (String) map.get("isdn");




        System.out.println("//////////////////////////////////////////////////////////////////////////////");
        if(isdn.equals("")){
            List<Number> numbers =  numberService.getByIMSI(imsi);

            if(numbers.size()>0){
                Number number=numbers.get(0);
                if(!number.getIsdn().equals("")){
                    String phoneNumber=number.getIsdn();
                    map.put("isdn",phoneNumber);
                    log.info("----------------------------------------------------");
                }
            }
        }
        log.debug("//////////////////////////////////////////////////////////////////////////////");
        //手机黑名单
        if (isTarget.equals("1")) {//如果是中标手机号码
            map.put("from", "mobile");
            //1.添加到系统告警表
            System.out.println("imsi：" + map.get("imsi") + "------");
            notificationService.addNotification(map);
            //2.添加到目标库
            if (null == targetService.getTargetByIMSI(imsi)) {
                targetService.addTargetByMobile(map);
            } else {//添加最新的目标
                targetService.deleteTargetByIMSI("imsi");
                targetService.addTargetByMobile(map);
            }

            sendToUser(isTarget, "admin");
        } else {
            List<Target> targets = new ArrayList<>();
            targets.add(targetService.getTargetByIMSI(imsi));
            if (!isdn.equals("")) {
                targets.addAll(targetService.getTargetByISDN(isdn));
            }
            if (!imei.equals("")) {
                targets.addAll(targetService.getTargetByIMEI(imei));
            }

            for (Target target : targets) {
                if (null != target) {//与电脑目标库匹配
                    map.put("from", "PC");
                    map.put("isTarget", 1);
                    //1.添加到系统告警表
                    notificationService.addNotification(map);
                    //2.推送到前台
                    sendToUser("目标已上号", "admin");
                    break;
                }
            }
        }
        numberService.addNumber(map);
        //1.取号信息 2.上号数量3.取号数量 4.运营商取号数量

        //系统取号总量
        int numberCount = numberService.getCountTotal();
        //系统上号数量
        int notificationCount = notificationService.getNotificationCount();
        //运营商取号数量
        int countOfCMCC = numberService.getCountByCMCC();
        int countOfCTCC = numberService.getCountByCTCC();
        int countOfCUCC = numberService.getCountByCUCC();

        Map<String, String> sendMap = new HashMap<>();
        //取号信息

        JSONArray jsonArray=new JSONArray();
        jsonArray.put(map);
        String messageNumber=jsonArray.toString();
        messageNumber=messageNumber.substring(1,messageNumber.length()-1);
        System.out.println(messageNumber);
        sendMap.put("number", messageNumber);



        //上号数量
        sendMap.put("notificationCount", String.valueOf(notificationCount));
        //取号数量
        sendMap.put("numberCount", String.valueOf(numberCount));
        sendMap.put("countOfCMCC", String.valueOf(countOfCMCC));
        sendMap.put("countOfCTCC", String.valueOf(countOfCTCC));
        sendMap.put("countOfCUCC", String.valueOf(countOfCUCC));

        int numberToday = numberService.getCountToday();
        int notificationToday = notificationService.getCountToday();
        sendMap.put("numberToday", String.valueOf(numberToday));
        sendMap.put("notificationToday", String.valueOf(notificationToday));


        JSONObject json = new JSONObject(sendMap);
        String data = json.toString();
        sendToUser(data, "screen");

    }

    /**
     * 暴露给外部的群发
     *
     * @param message
     * @throws IOException
     */
    public void sendInfo(String message) throws IOException {
        sendAll(message);
    }

    /**
     * 给所有人发消息
     *
     * @param message
     */
    public void sendAll(String message) {
        String now = getNowTime();
        String sendMessage = message.split("[|]")[0];
        //遍历HashMap
        for (String key : webSocketSet.keySet()) {
            try {
                //判断接收用户是否是当前发消息的用户
                if (!userno.equals(key)) {
                    webSocketSet.get(key).sendMessage(now + "用户" + userno + "发来消息：" + " <br/> " + sendMessage);
                    System.out.println("key = " + key);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 发生错误时调用
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("----websocket-------有异常啦");
        error.printStackTrace();
    }

    /**
     * 减少在线人数
     */
    private void subOnlineCount() {
        CustomWebSocket.onlineCount--;
    }

    /**
     * 添加在线人数
     */
    private void addOnlineCount() {
        CustomWebSocket.onlineCount++;
    }

    /**
     * 当前在线人数
     *
     * @return
     */
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    /**
     * 发送信息
     *
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        //获取session远程基本连接发送文本消息
        this.session.getBasicRemote().sendText(message);
        //this.session.getAsyncRemote().sendText(message);
    }


    /**
     * 给指定的人发送消息
     *
     * @param message
     */
    public static void sendToUser(String message, String userno) {
        try {
            if (webSocketSet.get(userno) != null) {
                webSocketSet.get(userno).sendMessage(message);
            } else {
                System.out.println("当前用户不在线");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取当前时间
     *
     * @return
     */
    private static String getNowTime() {
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(date);
        return time;
    }


}
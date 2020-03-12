package com.jsc.zao.service;

import com.jsc.zao.mapper.NotificationMapper;
import com.jsc.zao.mapper.NumberMapper;
import com.jsc.zao.util.CustomWebSocket;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class ScheduledService {
    @Autowired
    private NumberMapper numberMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    public void sendOnline(){
        Map<String,List> map=new HashMap<String,List>();
        Map<String,List> mapDay=findDay();
        map.put("dateList",mapDay.get("dateList"));
        map.put("perDayValue",mapDay.get("perDayValue"));

        Map<String,List> mapHour=findHour();
        map.put("timeList",mapHour.get("timeList"));
        map.put("timeValue",mapHour.get("timeValue"));




//        JSONObject jsonObject=new JSONObject(map);
//        String weeKData=jsonObject.toString();
//        System.out.println(weeKData);
//        CustomWebSocket.sendToUser(weeKData,"screen");

        //系统取号总量
        int numberCount = numberMapper.getCountTotal();
        //系统上号数量
        int notificationCount = notificationMapper.getNotificationCount();
        //运营商取号数量
        int countOfCMCC = numberMapper.getCountByCMCC();
        int countOfCTCC = numberMapper.getCountByCTCC();
        int countOfCUCC = numberMapper.getCountByCUCC();
        Map<String,String> sendMap=new HashMap<String,String>();
        //上号数量
        sendMap.put("notificationCount", String.valueOf(notificationCount));
        //取号数量
        sendMap.put("numberCount", String.valueOf(numberCount));
        sendMap.put("countOfCMCC", String.valueOf(countOfCMCC));
        sendMap.put("countOfCTCC", String.valueOf(countOfCTCC));
        sendMap.put("countOfCUCC", String.valueOf(countOfCUCC));

        int numberToday = numberMapper.getCountToday()==null?0:numberMapper.getCountToday();
        int notificationToday = notificationMapper.getCountToday()==null?0:notificationMapper.getCountToday();
        sendMap.put("numberToday", String.valueOf(numberToday));
        sendMap.put("notificationToday", String.valueOf(notificationToday));
        Map<String,Object> number=new HashMap<>();
        number.put("weekDay",map);
        number.put("startNumber",sendMap);
        JSONObject json = new JSONObject(number);
        String data = json.toString();
        CustomWebSocket.sendToUser(data, "screen");
        System.out.println("------------------------呜呜呜呜呜呜呜呜无无无无无无无无无无无"+data);
    }




    @Scheduled(cron = "0 0 * * * *")
    public void sendPerHour(){
       Map map=findHour();
       JSONObject jsonObject=new JSONObject(map);
       String hourData=jsonObject.toString();
        System.out.println(hourData);
        CustomWebSocket.sendToUser(hourData,"screen");
    }



    @Scheduled(cron = "0 0 0 * * *")
    public void sendPerDay(){
       Map map=findDay();
        JSONObject jsonObject=new JSONObject(map);
        String weeKData=jsonObject.toString();
        System.out.println(weeKData);
        CustomWebSocket.sendToUser(weeKData,"screen");
    }

    /**
     * 小时统计
     * @return
     */
    public Map<String, List> findHour() {
        Date today=new Date();
        SimpleDateFormat f=new SimpleDateFormat("HH");
        String time=f.format(today);
        Integer last=Integer.parseInt(time);
        ArrayList<Integer> timeList=new ArrayList();
        for(int i=23;i>-1;i--){
            if(last-i-1>-1)
                timeList.add((last-i-1));
        }
        ArrayList<Integer> timeValue = new ArrayList();
        for (int i = 0; i < timeList.size(); i++) {
            int count=numberMapper.findHour(timeList.indexOf(i))==null? 0:numberMapper.findHour(timeList.indexOf(i));
            timeValue.add(count);
        }
        System.out.println(timeList);
        System.out.println(timeValue);
        Map<String,List> map=new HashMap<>();
        map.put("timeList",timeList);
        map.put("timeValue",timeValue);
        return map;
    }

    /**
     * 周统计
     * @return
     */
    public Map<String, List> findDay() {
        Map<String,List> map= new HashMap<>();

        Date date=new Date();//取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");

        LinkedList<String> dateList=new LinkedList<String>();
        LinkedList<Integer>  perDayValue=new LinkedList<Integer>();
        for(int i=1;i<8;i++){

            calendar.add(calendar.DATE,-i);//把日期往后增加一天.整数往后推,负数往前移动
            date=calendar.getTime(); //这个时间就是日期往后推一天的结果
            calendar.add(calendar.DATE,i);//重置
            String dateString = formatter.format(date);
            dateList.push(dateString);

            int count=numberMapper.getCountByDay(i)==null ? 0 : numberMapper.getCountByDay(i);
            perDayValue.push(count);
        }
        System.out.println(dateList);
        System.out.println(perDayValue);
        map.put("dateList",dateList);
        map.put("perDayValue",perDayValue);
        return  map;
    }
}

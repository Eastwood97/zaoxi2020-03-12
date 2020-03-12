package com.jsc.zao.util.utils.log.aop;

import com.jsc.zao.bean.SysLog;
import com.jsc.zao.service.SystemLogService;
import com.jsc.zao.util.Util;
import com.jsc.zao.util.utils.IpUtils;
import com.jsc.zao.util.utils.JsonUtils;
import com.jsc.zao.util.utils.log.annotation.SystemServiceLog;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import sun.tools.jconsole.JConsole;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;

@Aspect
@Component
@SuppressWarnings("all")
public class SystemLogAspect {
//    //注入Service用于把日志保存数据库，实际项目入库采用队列做异步
    @Autowired
    private SystemLogService systemLogService;
    //本地异常日志记录对象
    private static final Logger logger = LoggerFactory.getLogger(SystemLogAspect.class);
    //Service层切点
    @Pointcut("@annotation(com.jsc.zao.util.utils.log.annotation.SystemServiceLog)")
    public void serviceAspect(){
    }

    //Controller层切点
    @Pointcut("@annotation(com.jsc.zao.util.utils.log.aop.SystemControllerLog)")
    public void controllerAspect(){
    }

    /**
     * @Description  前置通知  用于拦截Controller层记录用户的操作
     * @date 2018年9月3日 10:38
     */

    @Before("controllerAspect()")
    public void doBefore(JoinPoint joinPoint){
        System.out.println("2541324");
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        //读取session中的用户
//        User user = (User) session.getAttribute("username");

        String ip = IpUtils.getRealIP(request);

        try {
            //*========控制台输出=========*//
            System.out.println("==============前置通知开始==============");
            System.out.println("请求方法" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName()));
            System.out.println("方法描述：" + getControllerMethodDescription(joinPoint));
//            System.out.println("请求人："+user.getUsername());
            System.out.println(joinPoint.getArgs());

            //*========数据库日志=========*//
            SysLog sysLog = new SysLog();
            sysLog.setUsername(Util.getCurrentUser().getUsername());
            sysLog.setOperation(getControllerMethodDescription(joinPoint));
            sysLog.setMethod((joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName()));
            Object[] args = joinPoint.getArgs();
            sysLog.setParams(Arrays.toString(args));
            sysLog.setIp(ip);
            sysLog.setCreateDate(new Date());
//            Action action = new Action();
//            action.setActionDes(getControllerMethodDescription(joinPoint));
//            action.setActionType("0");
//            action.setActionIp(ip);
//            action.setUserId(user.getId());
//            action.setActionTime(new Date());
//            //保存数据库
//            actionService.add(action);
            System.out.println(sysLog);
            systemLogService.add(sysLog);
        }catch (Exception e){
            //记录本地异常日志
            e.printStackTrace();
            logger.error("==前置通知异常==");
            logger.error("异常信息：{}",e.getMessage());
        }
    }

    /**
     * @Description  异常通知 用于拦截service层记录异常日志
     * @date 2018年9月3日 下午5:43
     */
    @AfterThrowing(pointcut = "serviceAspect()",throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint,Throwable e){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        //读取session中的用户
//        User user = (User) session.getAttribute("user");
        //获取请求ip
        String ip = IpUtils.getRealIP(request);
        //获取用户请求方法的参数并序列化为JSON格式字符串
        String params = "";
        if (joinPoint.getArgs()!=null&&joinPoint.getArgs().length>0){
            for (int i = 0; i < joinPoint.getArgs().length; i++) {
                params+= JsonUtils.objectToJson(joinPoint.getArgs()[i])+";";
            }
        }
        try{
            /*========控制台输出=========*/
            System.out.println("=====异常通知开始=====");
            System.out.println("异常代码:" + e.getClass().getName());
            System.out.println("异常信息:" + e.getMessage());
            System.out.println("异常方法:" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));
            System.out.println("方法描述:" + getServiceMethodDescription(joinPoint));
            System.out.println("请求人:" + "");
            System.out.println("请求IP:" + ip);
            System.out.println("请求参数:" + params);
            /*==========数据库日志=========*/
//            Action action = new Action();
//            action.setActionDes(getServiceMethodDescription(joinPoint));
//            action.setActionType("1");
//            action.setUserId(user.getId());
//            action.setActionIp(ip);
//            action.setActionTime(new Date());
//            //保存到数据库
//            actionService.add(action);
        }catch (Exception ex){
            //记录本地异常日志
            logger.error("==异常通知异常==");
            logger.error("异常信息:{}", ex.getMessage());
        }
    }


    /**
     * @Description  获取注解中对方法的描述信息 用于service层注解
     * @date 2018年9月3日 下午5:05
     */
    public static String getServiceMethodDescription(JoinPoint joinPoint)throws Exception{
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String description = "";
        for (Method method:methods) {
            if (method.getName().equals(methodName)){
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length==arguments.length){
                    description = method.getAnnotation(SystemServiceLog.class).description();
                    break;
                }
            }
        }
        return description;
    }



    /**
     * @author changyaofang
     * @Description  获取注解中对方法的描述信息 用于Controller层注解
     * @date 2018年9月3日 上午12:01
     */
    public static String getControllerMethodDescription(JoinPoint joinPoint) throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();//目标方法名
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String description = "";
        for (Method method:methods) {
            if (method.getName().equals(methodName)){
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length==arguments.length){
                    description = method.getAnnotation(SystemControllerLog.class).description();
                    break;
                }
            }
        }
        return description;
    }
}

package com.jsc.zao.controller;

import com.jsc.zao.bean.Notification;
import com.jsc.zao.bean.RespBean;
import com.jsc.zao.mapper.NotificationMapper;
import com.jsc.zao.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/sysmsgs")
public class SystemMsgController {

    @Autowired
    NotificationService notificationService;

    @RequestMapping(value = "/notification",method = RequestMethod.GET)
    public Map<String, Object> getNotificationByPage(@RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "size", defaultValue = "10") Integer size) {

        List<Notification> notifications=notificationService.getNotificationByPage(page,size);
        int count=notificationService.getNotificationCount();
        Map<String,Object> map=new HashMap<>();
        map.put("notifications",notifications);
        map.put("count",count);
        return  map;
    }

    @RequestMapping(value = "/markread", method = RequestMethod.PUT)
    public RespBean markRead(int flag) {
        if (notificationService.markRead(flag)) {
            if (flag == -1) {
                return new RespBean("success","已读全部");
            } else {
                return new RespBean("success","已读一条");
            }
        } else{
            return new RespBean("success","通知已读");
        }
    }
}

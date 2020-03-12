package com.jsc.zao.service;

import com.jsc.zao.bean.Notification;
import com.jsc.zao.bean.Target;
import com.jsc.zao.mapper.NotificationMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class NotificationService {

    @Autowired
    NotificationMapper notificationMapper;

    public List<Notification> getNotificationByPage(Integer page, Integer size) {
        int start=(page-1)*size;
        return notificationMapper.getNotificationByPage(start,size);
    }


    public int getNotificationCount(){
        return  notificationMapper.getNotificationCount();
    }

    public boolean markRead(int flag){
        if (flag != -1) {
            return notificationMapper.markRead(flag)==1;
        }
        notificationMapper.markRead(flag);
        return true;
    }

    public  void addNotification(Map map){
        notificationMapper.addNotification(map);
    }

    public List<Notification> getByCaseName(String caseName) {
        return notificationMapper.getByCaseName(caseName);
    }

    public int getCountToday(){
        return  notificationMapper.getCountToday()==null ? 0 :notificationMapper.getCountToday();
    }
}

package com.jsc.zao.mapper;

import com.jsc.zao.bean.Notification;
import com.jsc.zao.service.NotificationService;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface NotificationMapper {
     List<Notification> getNotificationByPage(@Param("start") Integer start, @Param("size") Integer size);


    int getNotificationCount();

    int markRead(@Param("flag") int flag);

    void addNotification(Map map);

    List<Notification> getByCaseName(String caseName);

    Integer getCountToday();
}

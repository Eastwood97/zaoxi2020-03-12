package com.jsc.zao.controller;

import com.jsc.zao.bean.Notification;
import com.jsc.zao.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/map")
public class MapController {

    @Autowired
    NotificationService notificationService;

    @RequestMapping(value = "/pitchByCaseName")
    public List<Notification> pitchByCaseName(@RequestParam("caseName") String caseName){
        return notificationService.getByCaseName(caseName);
    }
}

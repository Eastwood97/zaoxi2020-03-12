package com.jsc.zao.controller;


import com.fasterxml.jackson.annotation.JacksonAnnotation;
import com.jsc.zao.bean.Number;
import com.jsc.zao.bean.RespBean;
import com.jsc.zao.service.NumberService;
import com.jsc.zao.util.poi.PoiNumberUtil;
import com.jsc.zao.util.utils.log.aop.SystemControllerLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 取号控制层
 */
@RestController
@RequestMapping("/basicdata")
public class NumberController {

    @Autowired
    NumberService numberService;

    @RequestMapping(value = "/number" ,method = RequestMethod.GET)
    @SystemControllerLog(description = "查询取号信息")
    public Map<String,Object> getNumbersByPage(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size,
                                               String imsi,String imei,String isdn,String area, String  isTarget,String caseName){
        Map<String,Object> map=new HashMap<String,Object>();
        List<Number> numbers=numberService.getNumbersByPage(page,size,imsi,imei,isdn,area,isTarget,caseName);
        int count =numberService.getCountByKeywords(imsi,imei,isdn,area,isTarget,caseName);
        map.put("numbers",numbers);
        map.put("count",count);
        return map;
    }

    @RequestMapping(value = "/number/{ids}",method = RequestMethod.DELETE)
    @SystemControllerLog(description = "删除取号信息")
    public RespBean deleteNumById(@PathVariable String ids){
        if(numberService.deleteNumById(ids)){
            return new RespBean("success","删除成功");
        }else{
            return new RespBean("error","删除失败");
        }
    }

    @RequestMapping(value = "/number/exportNumber", method = RequestMethod.GET)
    @SystemControllerLog(description = "导出取号信息")
    public ResponseEntity<byte[]> exportEmp() {
        return PoiNumberUtil.exportNumberExcel(numberService.getAllNumbers());
    }
}

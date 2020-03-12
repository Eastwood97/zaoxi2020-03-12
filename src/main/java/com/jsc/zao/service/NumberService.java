package com.jsc.zao.service;

import com.jsc.zao.bean.Number;
import com.jsc.zao.mapper.NumberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class NumberService {

    @Autowired
    NumberMapper numberMapper;

    public List<Number> getNumbersByPage(Integer page, Integer size, String imsi, String imei, String isdn, String area,  String  isTarget, String caseName){
        int start=(page-1)*size;
        System.out.println("______________________________________________"+isTarget);
        return numberMapper.getNumbersByPage(start,size,imsi,imei,isdn,area,isTarget,caseName);
    }

    public  int getCountByKeywords(String imsi,String imei,String isdn,String area, String  isTarget,String caseName){
        return  numberMapper.getCountByKeywords(imsi,imei,isdn,area,isTarget,caseName);
    }

    public  boolean deleteNumById(String  ids){
        String[] split = ids.split(",");
        int result=numberMapper.deleteNumById(split);
        return result == split.length;
    }

    public  void  addNumber(Map map){
        numberMapper.addNumber(map);
    }

    public List<Number> getAllNumbers() {
        return numberMapper.getNumbersByPage(null,null,null,null,null,null,null,null);
    }

    public int getCountByCTCC(){
        return numberMapper.getCountByCTCC();
    }

    public int getCountByCMCC(){
        return numberMapper.getCountByCMCC();
    }

    public int getCountByCUCC(){
        return numberMapper.getCountByCUCC();
    }

    public int getCountToday(){
       return numberMapper.getCountToday()==null ? 0 :numberMapper.getCountToday();
    }

    public List<Number> getByIMSI(String imsi) {
        return  numberMapper.getByIMSI(imsi);
    }


    public int getCountTotal() {
        return numberMapper.getCountTotal();
    }
}

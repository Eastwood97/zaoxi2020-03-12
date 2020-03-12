package com.jsc.zao.service;

import com.jsc.zao.bean.Target;
import com.jsc.zao.mapper.TargetMapper;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class TargetService {

    @Autowired
    TargetMapper targetMapper;

    public List<Target> getTargetByPage(Integer page ,  Integer size, String imsi,  String isdn, String imei,  String name,  String caseName){
        int start=(page-1)*size;
        return  targetMapper.getTargetByPage(start,size,imsi,isdn,imei,name,caseName);
    }

    public int getTargetCount(  String imsi,String isdn, String imei,  String name,  String caseName){
        return targetMapper.getTargetCount(imsi,isdn,imei,name,caseName);
    }

    public boolean deleteTargetByID(String  ids){
        String[] split = ids.split(",");
        int result=targetMapper.deleteTargetByID(split);
        return result == split.length;
    }

    public int addTargetByPC(Target target){
        String imei=target.getImei();
        if(null!=imei&&!imei.equals("")){
            imei=imei.substring(0,imei.length()-1).concat("0");
        }
        target.setImei(imei);
        return  targetMapper.addTargetByPC(target);
    }

    public void addTargetByMobile(Map map){
        if(null==targetMapper.getTargetByIMSI((String) map.get("imsi"))){
            targetMapper.addTargetByMobile(map);
        }

    }

    public int updateTarget(Target target){
       return targetMapper.updateTarget(target);
    }

    public List<Target> getAllTargets() {
        return targetMapper.getTargetByPage(null,null,null,null,null,null,null);
    }

    public Target getTargetByIMSI(String imsi) {
        return targetMapper.getTargetByIMSI(imsi);
    }

    public List<Target> getTargetByISDN(String isdn) {
        return targetMapper.getTargetByISDN(isdn);
    }

    public List<Target> getTargetByIMEI(String imei) {
        return targetMapper.getTargetByIMEI(imei);
    }


    public void deleteTargetByIMSI(String imsi) {
        targetMapper.deleteTargetByIMSI(imsi);
    }

    public boolean insertForeach(List<Target> targetList){
        int result=targetMapper.insertForeach(targetList);
        if(result==targetList.size()){
            return true;
        }else {
            return false;
        }

    }
}

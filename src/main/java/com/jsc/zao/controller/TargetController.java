package com.jsc.zao.controller;


import com.jsc.zao.bean.RespBean;
import com.jsc.zao.bean.Target;
import com.jsc.zao.service.ImportService;
import com.jsc.zao.service.TargetService;
import com.jsc.zao.util.poi.PoiNumberUtil;
import com.jsc.zao.util.poi.PoiTargetUtil;
import com.jsc.zao.util.utils.log.aop.SystemControllerLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 目标库控制层
 */
@RestController
@RequestMapping(value = "/basicdata")
public class TargetController {

    @Autowired
    private TargetService targetService;

    @Autowired
    private ImportService importService;

    @RequestMapping(value = "/target" , method = RequestMethod.GET)
    @SystemControllerLog(description = "查询目标库")
    public Map<String,Object> getListsByPage(@RequestParam(defaultValue = "1") Integer page,
                                             @RequestParam(defaultValue = "10") Integer size,
                                             String imsi,  String isdn, String imei,  String name,  String caseName){

        Map<String,Object> map=new HashMap<String,Object>();
        List<Target>  targets=targetService.getTargetByPage(page,size,imsi,isdn,imei,name,caseName);
        int count =targetService.getTargetCount(imsi,isdn,imei,name,caseName);
        map.put("targets",targets);
        map.put("count",count);
        return map;
    }

    @RequestMapping(value = "/target/{ids}",method = RequestMethod.DELETE)
    @SystemControllerLog(description = "删除目标")
    public RespBean deleteBlackList(@PathVariable("ids") String ids){
        if (targetService.deleteTargetByID(ids)){
            return new RespBean("success","删除成功！");
        }else{
            return  new RespBean("error","删除失败！");
        }
    }

    @RequestMapping(value = "/target",method = RequestMethod.POST)
    @SystemControllerLog(description = "添加布控名单")
    public  RespBean addTargetByPC(Target target){
        if (targetService.addTargetByPC(target)==1){
            return new RespBean("success","添加成功！");
        }else{
            return  new RespBean("error","添加失败！");
        }
    }

    @RequestMapping(value = "/target",method = RequestMethod.PUT)
    @SystemControllerLog(description = "编辑目标信息")
    public  RespBean updateTarget(Target target){
        if (targetService.updateTarget(target)==1){
            return new RespBean("success","修改成功！");
        }else{
            return  new RespBean("error","修改失败！");
        }
    }

    @RequestMapping(value = "/target/exportTarget", method = RequestMethod.GET)
    @SystemControllerLog(description = "导出目标库")
    public ResponseEntity<byte[]> exportTarget() {
        return PoiTargetUtil.exportTargetExcel(targetService.getAllTargets());
    }

    /**
     *
     * 功能描述: 上传文件excl
     *
     * @param:
     * @return:
     * @auther: ww
     * @date: 2019/12/11 0011 13:44
     */
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    @ResponseBody
    public RespBean uploadExcel(HttpServletRequest request) throws Exception {
        try{

            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest )request;
            MultipartFile file = multipartRequest.getFile("file");
            String peopleid = multipartRequest.getParameter("body");
            if (file.isEmpty()) {
                return new RespBean("error","修改失败!" );
            }
            InputStream inputStream = file.getInputStream();
            List<List<Object>> list = importService.getBankListByExcel(inputStream, file.getOriginalFilename());
            inputStream.close();
            Target target=null;
            List<Target> targetList=new ArrayList<Target>();
            for (int i = 0; i < list.size(); i++) {
                List<Object> lo = list.get(i);
                String imsi = lo.get(0).toString();
                String imei = lo.get(1).toString();
                String  isdn = lo.get(2).toString();
                String name = lo.get(3).toString();
                String caseName = lo.get(4).toString();
                String desc = lo.get(5).toString();
                if(imsi.equals("")&&imei.equals("")&&isdn.equals("")){
                    return new RespBean("501", "imsi,imei,isdn不能都为空!");
                }
               target=new Target();
                target.setImei(imei);
                target.setImsi(imsi);
                target.setIsdn(isdn);
                target.setName(name);
                target.setDesc(desc);
                target.setCaseName(caseName);
                target.setFrom("PC");
               targetList.add(target);
            }
            if (targetService.insertForeach(targetList)){
                return new RespBean("success", "上传成功!");
            }else{
                return new RespBean("error", "上传失败!");
            }
        }catch (Exception e){
            return new RespBean("error", "上传失败!");

        }
    }
}

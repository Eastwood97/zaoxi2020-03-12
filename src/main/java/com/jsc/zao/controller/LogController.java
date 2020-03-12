package com.jsc.zao.controller;

import com.jsc.zao.bean.SearchSysLog;
import com.jsc.zao.service.SystemLogService;
import com.jsc.zao.util.utils.PageBean;
import com.jsc.zao.util.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/log")
public class LogController {

    @Autowired
    private SystemLogService systemLogService;


    @RequestMapping(value = "/delete/{ids}",method = RequestMethod.DELETE)
    public Result deleteSysLog(@PathVariable String ids){
        try {
            String replace = ids.replace("[", "").replace("]","");
            String[] split = replace.split(",");
            for (String str :split){
                systemLogService.delete(Integer.parseInt(str));
            }
            return new Result("success","操作成功！");
        }catch (Exception e){
            e.printStackTrace();
            return new Result("error","操作失败！");
        }
    }
    @RequestMapping(value = "/getSysLogAll",method = RequestMethod.GET)
        public PageBean getSysLogAll(SearchSysLog searchSysLog){
            return systemLogService.getSysLogAll(searchSysLog);
    }
}
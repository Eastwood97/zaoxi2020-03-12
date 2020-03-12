package com.jsc.zao.service;

import com.jsc.zao.bean.SearchSysLog;
import com.jsc.zao.bean.SysLog;
import com.jsc.zao.util.utils.PageBean;

public interface SystemLogService {
    void add(SysLog sysLog);
    void delete(Integer id);
    PageBean getSysLogAll(SearchSysLog searchSysLog);
}

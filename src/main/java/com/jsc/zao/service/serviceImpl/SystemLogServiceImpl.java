package com.jsc.zao.service.serviceImpl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jsc.zao.bean.SearchSysLog;
import com.jsc.zao.bean.SysLog;
import com.jsc.zao.mapper.SysLogMapper;
import com.jsc.zao.service.SystemLogService;
import com.jsc.zao.util.utils.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemLogServiceImpl implements SystemLogService {
    @Autowired
    private SysLogMapper sysLogMapper;

    @Override
    public void delete(Integer id) {
        sysLogMapper.deleteSysLog(id);
    }

    @Override
    public void add(SysLog sysLog) {
        sysLogMapper.insertSysLog(sysLog);
    }

    @Override
    public PageBean getSysLogAll(SearchSysLog searchSysLog) {
        PageHelper.startPage(Integer.parseInt(searchSysLog.getPageCode()), Integer.parseInt(searchSysLog.getPageSize()));
        Page<SysLog> page = (Page<SysLog>) sysLogMapper.getSysLogAll(searchSysLog.getUsername(), searchSysLog.getCreateDate());
        return new PageBean(page.getTotal(), page.getResult());
    }
}

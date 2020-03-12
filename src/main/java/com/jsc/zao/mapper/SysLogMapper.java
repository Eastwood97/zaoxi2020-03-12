package com.jsc.zao.mapper;
import com.jsc.zao.bean.SysLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface SysLogMapper {
    int insertSysLog(SysLog sysLog);
    int deleteSysLog(Integer id);
    List<SysLog> getSysLogAll(String username, Date createDate);
}

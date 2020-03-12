package com.jsc.zao.mapper;

import com.jsc.zao.bean.Target;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface TargetMapper {
    List<Target> getTargetByPage(@Param("start") Integer start , @Param("size") Integer size,
                                 @Param("imsi") String imsi, @Param("isdn") String isdn,
                                 @Param("imei") String imei,@Param("name") String name,@Param("caseName") String caseName);

    int getTargetCount( @Param("imsi") String imsi, @Param("isdn") String isdn,
                        @Param("imei") String imei,@Param("name") String name,@Param("caseName") String caseName);

    int deleteTargetByID(@Param("ids") String [] ids);

    int addTargetByPC(Target target);

    void addTargetByMobile(Map map);

    int updateTarget(Target target);

    Target getTargetByIMSI(String imsi);

    List<Target> getTargetByISDN(String isdn);

    List<Target> getTargetByIMEI(String imei);

    void deleteTargetByIMSI(String imsi);

    int insertForeach(List<Target> list);
}

package com.jsc.zao.mapper;

import com.jsc.zao.bean.Number;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.omg.PortableInterceptor.INACTIVE;

import java.util.List;
import java.util.Map;

@Mapper
public interface NumberMapper {


    List<Number> getNumbersByPage(@Param("start") Integer start,
                                  @Param("size") Integer size,
                                  @Param("imsi") String imsi,
                                  @Param("imei") String imei,
                                  @Param("isdn") String isdn,
                                  @Param("area") String area,
                                  @Param("isTarget")  String  isTarget,
                                  @Param("caseName") String caseName
    );



    int  getCountByKeywords(
            @Param("imsi") String imsi,
            @Param("imei") String imei,
            @Param("isdn") String isdn,
            @Param("area") String area,
            @Param("isTarget")  String  isTarget,
            @Param("caseName") String caseName);

    int deleteNumById(@Param("ids") String [] ids);
    void addNumber(Map map);

    @Select(value = "SELECT count(DISTINCT `imsi`) FROM number" )
    int getCountTotal();
    int getCountByCTCC();

    int getCountByCMCC();

    int getCountByCUCC();

    Integer getCountByDay(int day);

    @Select(value = "SELECT count(*) FROM(SELECT catchTime FROM number \n" +
            "WHERE TIMESTAMPDIFF(SECOND,catchTime,NOW())<3600) as temp")
    Integer findByHour();

    @Select(value = "SELECT  count(*) from( SELECT DISTINCT(imsi) from number WHERE TIMESTAMPDIFF(DAY,catchTime,NOW())=0 AND HOUR(catchTime)=#{interval})as temp")
    Integer findHour(int interval);

    Integer getCountToday();

    List<Number> getByIMSI(String imsi);
}

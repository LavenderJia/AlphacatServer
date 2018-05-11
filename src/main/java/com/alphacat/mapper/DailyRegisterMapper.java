package com.alphacat.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyRegisterMapper {

    /** 检查某天的签到记录是否存在*/
    @Select("SELECT count(*) FROM daily_register WHERE workerId=#{workerId} AND date=#{date}")
    boolean hasRecord(@Param("workerId") int workerId, @Param("date") String date);

    /** 新增一条签到记录*/
    @Insert("INSERT INTO daily_register(workerId, date) VALUES(#{workerId},CURDATE())")
    void addRecord(int workerId);
}

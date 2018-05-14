package com.alphacat.mapper;

import com.alphacat.pojo.WorkerCredit;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkerCreditMapper {
    /** 新增一条积分明细记录 */
    @Insert("INSERT INTO worker_credit(workerId, taskId, valueChange, date) VALUES(#{workerId},#{taskId},#{valueChange},#{date})")
    void add(WorkerCredit record);
    /** 获取工人的历史明细记录 */
    @Select("SELECT * FROM worker_credit WHERE workerId=#{workerId}")
    List<WorkerCredit> get(@Param("workerId") int workerId);
}

package com.alphacat.mapper;

import com.alphacat.pojo.RequesterCredit;
import com.alphacat.pojo.WorkerCredit;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditMapper {
    /**
     * Add a worker credit transaction record.
     */
    @Insert("INSERT INTO credit_transaction(workerId, taskId, `change`, date, credit) " +
            "VALUES(#{workerId}, #{taskId}, #{change}, #{date}, #{credit})")
    void add(WorkerCredit record);

    @Select("SELECT workerId, taskId, name taskName, `change`, date, credit " +
            "FROM (" +
                "SELECT * FROM credit_transaction WHERE workerId=#{workerId} " +
            ") c JOIN task ON taskId = id " +
            "ORDER BY date DESC")
    List<WorkerCredit> getWorkerCredits(@Param("workerId") int workerId);

    /**
     * Order is date descending.
     */
    @Select("SELECT id, name, MAX(date) date, SUM(`change`) sum " +
            "FROM (" +
                "SELECT id, name FROM task WHERE requesterId = #{requesterId}" +
            ") t JOIN credit_transaction ON id = taskId " +
            "GROUP BY id " +
            "ORDER BY date DESC")
    List<RequesterCredit> getRequesterCredits(@Param("requesterId") int requesterId);
}

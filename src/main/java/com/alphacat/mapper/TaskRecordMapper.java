package com.alphacat.mapper;

import com.alphacat.pojo.TaskRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRecordMapper {

    /**
     * Get all task records of a worker.
     */
    @Select("SELECT * FROM task_record WHERE workerId=#{workerId}")
    List<TaskRecord> getByWorker(int workerId);

    @Select("SELECT * FROM task_record WHERE taskId = #{taskId}")
    List<TaskRecord> getByTask(@Param("taskId") int taskId);

    /**
     * Get all task ids of tasks that the worker did.
     */
    @Select("SELECT taskId FROM task_record WHERE workerId=#{workerId}")
    int[] getTaskIds(int workerId);

    /**
     * Get the number of workers in a task.
     */
    @Select("SELECT COUNT(DISTINCT workerId) FROM task_record WHERE taskId=#{taskId}")
    Integer getWorkerNum(int taskId);

    /**
     * Get the total number of tags of a task.
     */
    @Select("SELECT SUM(picDoneNum) FROM task_record WHERE taskId=#{taskId}")
    Integer getTagSum(int taskId);

    /**
     * Get a certain task record according to given workerId and taskId.
     */
    @Select("SELECT * FROM task_record WHERE workerId=#{workerId} AND taskId=#{taskId}")
    TaskRecord get(@Param("workerId") int workerId, @Param("taskId") int taskId);

    @Insert("INSERT INTO task_record(workerId, taskId, picOrder, " +
            "picDoneNum) VALUES(#{workerId},#{taskId},#{picOrder},#{picDoneNum})")
    void add(TaskRecord taskRecord);

    /**
     * Increment the picDoneNum of a task record.
     */
    @Update("UPDATE task_record SET picDoneNum=picDoneNum+1 " +
            "WHERE workerId=#{workerId} AND taskId=#{taskId}")
    void incPicDoneNum(@Param("workerId") int workerId, @Param("taskId") int taskId);

    /**
     * Decrement the picDoneNum of a task record.
     */
    @Update("UPDATE task_record SET picDoneNum=picDoneNum-1 " +
            "WHERE workerId=#{workerId} AND taskId=#{taskId}")
    void decPicDoneNum(@Param("workerId") int workerId, @Param("taskId") int taskId);

}

package com.alphacat.mapper;

import com.alphacat.pojo.HistoryTask;
import com.alphacat.pojo.Task;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskMapper {

    /**
     * Generate a new Id for a new task.
     */
    @Select("SELECT MAX(id)+1 FROM task")
    Integer getNewId();

    /**
     * Get all tasks published by a given requester.
     */
    @Select("SELECT * FROM task WHERE requesterId=#{requesterId}")
    List<Task> getByRequester(@Param("requesterId") int requesterId);

    /**
     * Get available tasks of 'today'.
     */
    @Select("SELECT * FROM task WHERE NOW()>startTime AND endTime>DATE_SUB(CURDATE(),INTERVAL 1 DAY)")
    List<Task> getAvailableTask();

    /**
     * Get all tasks that has been finished by a worker.
     */
    @Select("SELECT A.id, A.name, A.startTime, A.endTime, B.creditEarned " +
            "FROM task AS A JOIN (" +
                "SELECT taskId AS id, SUM(valueChanged) AS creditEarned " +
                "FROM worker_credit WHERE workerId=#{workerId} AND taskId=#{id}" +
            ") AS B ON A.id=B.id")
    List<HistoryTask> getHistoryTasks(@Param("taskId") int taskId,
                                      @Param("workerId") int workerId);

    /**
     * Get a task by its id.
     */
    @Select("SELECT * FROM task WHERE id=#{taskId}")
    Task get(@Param("taskId") int taskId);

    @Insert("INSERT INTO task(id, requesterId, name, description, creditPerPic, " +
            "creditFinished, method, hasWholeLabel, startTime, endTime) " +
            "VALUES(#{id},#{requesterId},#{name},#{description},#{creditPerPic}," +
            "#{creditFinished},#{method},#{hasWholeLabel},#{startTime},#{endTime})")
    void add(Task task);

    @Update("UPDATE task SET name=#{name}, description=#{description}, " +
            "creditPerPic=#{creditPerPic}, creditFinished=#{creditFinished}, " +
            "method=#{method}, hasWholeLabel=#{hasWholeLabel}, " +
            "startTime=#{startTime}, endTime=#{endTime} WHERE id=#{id}")
    void update(Task task);

    @Delete("DELETE FROM task WHERE id=#{taskId}")
    void delete(@Param("taskId") int taskId);

    @Select("SELECT * FROM task WHERE requesterId=#{requesterId} AND " +
            "endTime<DATE_SUB(CURDATE(),INTERVAL 1 DAY)")
    List<Task> getFinishedByRequester(@Param("requesterId") int requesterId);
}

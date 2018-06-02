package com.alphacat.mapper;

import com.alphacat.pojo.*;
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
    @Select("SELECT id, name, (" +
                "CASE " +
                "WHEN NOW() < startTime THEN 0 " +
                "WHEN NOW() > startTime AND endTime > DATE_SUB(CURDATE(), INTERVAL 1 DAY) " +
                    "THEN 100 * DATEDIFF(CURDATE(), startTime) / DATEDIFF(endTime, startTime) " +
                "WHEN endTime < DATE_SUB(CURDATE(), INTERVAL 1 DAY) THEN 100 " +
                "END" +
            ") state, COUNT(workerId) workerCount " +
            "FROM task LEFT JOIN task_record ON id = taskId " +
            "WHERE requesterId = #{requesterId}" +
            "GROUP BY id")
    List<RequesterTask> getByRequester(@Param("requesterId") int requesterId);

    @Select("SELECT id, name, 0 state, 0 workerCount FROM task " +
            "WHERE requesterId = #{requesterId} AND NOW() < startTime")
    List<RequesterTask> getIdleTasks(@Param("requesterId") int requesterId);

    @Select("SELECT id, name, (100 * DATEDIFF(CURDATE(), startTime) / DATEDIFF(endTime, startTime)) state, " +
                "COUNT(workerId) workerCount " +
            "FROM task LEFT JOIN task_record ON id = taskId " +
            "WHERE requesterId = #{requesterId} AND NOW() > startTime " +
                "AND endTime > DATE_SUB(CURDATE(), INTERVAL 1 DAY) " +
            "GROUP BY id")
    List<RequesterTask> getUnderwayTasks(@Param("requesterId") int requesterId);

    @Select("SELECT id, name, 100 state, COUNT(workerId) workerCount " +
            "FROM task LEFT JOIN task_record ON id = taskId " +
            "WHERE requesterId = #{requesterId} AND endTime < DATE_SUB(CURDATE(), INTERVAL 1 DAY) " +
            "GROUP BY id")
    List<RequesterTask> getEndedTask(@Param("requesterId") int requesterId);

    /**
     * This method is not requester-related. It retrieves all data.
     * It is meant to get used to add schedule jobs.
     */
    @Select("SELECT * FROM task WHERE endTime > DATE_SUB(CURDATE(), INTERVAL 1 DAY)")
    List<Task> getNotEnded();

    /**
     * Retrieve tasks that the worker DOES NOT take part in.
     * @see #getPartakingTask(int)
     */
    @Select("SELECT id, name, creditPerPic, creditFinished, method, endTime " +
            "FROM (" +
                "SELECT * FROM task WHERE NOW() > startTime " +
                "AND endTime > DATE_SUB(CURDATE(), INTERVAL 1 DAY)" +
            ") a WHERE id NOT IN(" +
                "SELECT taskId FROM task_record WHERE workerId=#{workerId}" +
            ")")
    List<AvailableTask> getAvailableTask(@Param("workerId") int workerId);

    /**
     * Tasks for testing purpose, test for workers' accuracy.
     * They are tasks that are published by 0-id requester(system requester).
     */
    @Select("SELECT id, name, creditPerPic, creditFinished, method, endTime " +
            "FROM task WHERE requesterId = 0")
    List<AvailableTask> getTestTask();

    /**
     * Retrieve tasks that the worker DOES take part in.
     * @see #getAvailableTask(int)
     */
    @Select("SELECT id, name, creditPerPic, creditFinished, method, endTime " +
            "FROM (" +
                "SELECT taskId FROM task_record WHERE workerId=#{workerId}" +
            ") a JOIN (" +
                "SELECT * FROM task WHERE NOW() > startTime " +
                "AND endTime > DATE_SUB(CURDATE(), INTERVAL 1 DAY)" +
            ") b ON taskId = id")
    List<AvailableTask> getPartakingTask(@Param("workerId") int workerId);

    @Select("SELECT id, name, endTime, earnedCredit, correctRate " +
            "FROM (" +
                "SELECT * FROM (" +
                    "SELECT * FROM task WHERE CURDATE() > endTime" +
                ") a JOIN (" +
                    "SELECT taskId, correctRate FROM task_record " +
                    "WHERE workerId = #{workerId} AND correctRate IS NOT NULL" +
                ") b ON id = taskId" +
            ") c JOIN (" +
                "SELECT taskId, SUM(`change`) earnedCredit " +
                "FROM credit_transaction WHERE workerId = #{workerId} " +
                "GROUP BY taskId" +
            ") d ON id = d.taskId")
    List<HistoryTask> getHistoryTasks(@Param("workerId") int workerId);

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

}

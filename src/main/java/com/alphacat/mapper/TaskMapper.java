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
    @Select("SELECT * FROM task WHERE requesterId=#{requesterId}")
    List<Task> getByRequester(@Param("requesterId") int requesterId);

    @Select("SELECT id, name, creditPerPic, creditFinished, method, startTime, endTime " +
            "FROM task WHERE requesterId = #{requesterId} AND NOW() < startTime")
    List<IdleTask> getIdleTasks(@Param("requesterId") int requesterId);

    @Select("SELECT id, name, startTime, endTime, workerCount, a.tagCount/count(p.pidx) tagRate " +
            "FROM (" +
                "SELECT id, name, startTime, endTime, COUNT(workerId) workerCount, " +
                    "IFNULL(SUM(picDoneNum), 0) tagCount " +
                "FROM ( " +
                    "SELECT * FROM task WHERE requesterId = #{requesterId} AND " +
                        "NOW() > startTime AND endTime > DATE_SUB(CURDATE(), INTERVAL 1 DAY)" +
                ") t LEFT JOIN task_record ON id = taskId " +
                "GROUP BY id" +
            ") a LEFT JOIN picture p ON id = taskId " +
            "GROUP BY id")
    List<UnderwayTask> getUnderwayTasks(@Param("requesterId") int requesterId);

    @Select("SELECT id, name, startTime, endTime, workerCount, tagRate, costCredit " +
            "FROM (" +
                "SELECT id, name, startTime, endTime, workerCount, a.tagCount/COUNT(p.pidx) tagRate " +
                "FROM (" +
                    "SELECT id, name, startTime, endTime, COUNT(workerId) workerCount, " +
                        "IFNULL(SUM(picDoneNum), 0) tagCount " +
                    "FROM (" +
                        "SELECT * FROM task WHERE requesterId = #{requesterId} AND " +
                            "CURDATE() > endTime" +
                    ") t LEFT JOIN task_record ON id = taskId" +
                ") a LEFT JOIN picture p ON id = taskId " +
                "GROUP BY id" +
            ") b LEFT JOIN (" +
                "SELECT taskId, IFNULL(SUM(`change`), 0) costCredit " +
                "FROM credit_transaction " +
                "GROUP BY taskId" +
            ") c ON id = taskId")
    List<EndedTask> getEndedTask(@Param("requesterId") int requesterId);

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

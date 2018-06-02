package com.alphacat.mapper;

import com.alphacat.pojo.Notice;
import com.alphacat.pojo.UserNoticeBrief;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeMapper {

    @Select("SELECT MAX(id)+1 FROM notice")
    Integer getNewId();

    @Insert("INSERT INTO notice(id, startDate, endDate, type, title, content) " +
            "VALUES (#{id}, #{startDate}, #{endDate}, #{type}, #{title}, #{content})")
    void add(Notice notice);

    @Delete("DELETE FROM notice WHERE id = #{id}")
    void delete(@Param("id") int id);

    @Select("SELECT * FROM notice WHERE id = #{id}")
    Notice get(@Param("id") int id);

    /**
     * Retrieve all notices that are shown to super admin.
     * @see #getByType(int) to retrieve notices that are shown to normal admin
     */
    @Select("SELECT * FROM notice")
    List<Notice> getAll();

    /**
     * Retrieve notices that are shown to admin except for super admin.
     * It will always return no-type notices.
     * For super admin's notice retrieving, use #getAll()
     * @param type
     *      0 for no type, 1 for worker type, 2 for requester type
     * @see #getAll() to retrieve notices that are shown to super admin
     */
    @Select("SELECT * FROM notice " +
            "WHERE type = #{type} OR type = 0")
    List<Notice> getByType(@Param("type") int type);

    /**
     * Get notices that are shown to workers.
     * It will not retrieve those that have ended.
     */
    @Select("SELECT id, title, startDate date, COUNT(workerId) state " +
            "FROM (" +
                "SELECT * FROM notice " +
                "WHERE endDate > DATE_SUB(CURDATE(), INTERVAL 1 DAY)" +
            ") n LEFT JOIN (" +
                "SELECT * FROM worker_notice_read " +
                "WHERE workerId = #{workerId}" +
            ") r ON id = noticeId " +
            "WHERE type = 0 OR type = 2 " +
            "GROUP BY id")
    List<UserNoticeBrief> getWorkerEmails(@Param("workerId") int workerId);

    /**
     * Get notices that are shown to requesters.
     * It will not retrieve those that have ended.
     */
    @Select("SELECT id, title, startDate date, COUNT(requesterId) state " +
            "FROM (" +
                "SELECT * FROM notice " +
                "WHERE endDate > DATE_SUB(CURDATE(), INTERVAL 1 DAY)" +
            ") n LEFT JOIN (" +
                "SELECT * FROM requester_notice_read " +
                "WHERE requesterId = #{requesterId}" +
            ") r ON id = noticeId " +
            "WHERE type = 0 OR type = 1 " +
            "GROUP BY id")
    List<UserNoticeBrief> getRequesterEmails(@Param("requesterId") int requesterId);

    /**
     * Check existence before add new record.
     */
    @Select("SELECT COUNT(*) FROM worker_notice_read " +
            "WHERE workerId = #{workerId} AND noticeId = #{noticeId}")
    boolean checkWorkerRead(@Param("workerId") int workerId,
                                @Param("noticeId") int noticeId);

    @Insert("INSERT INTO worker_notice_read(workerId, noticeId) " +
            "VALUES(#{workerId}, #{noticeId})")
    void addWorkerReadRecord(@Param("workerId") int workerId,
                             @Param("noticeId") int noticeId);

    /**
     * Check existence before add new record.
     */
    @Select("SELECT COUNT(*) FROM requester_notice_read " +
            "WHERE requesterId = #{requesterId} AND noticeId = #{noticeId}")
    boolean checkRequesterRead(@Param("requesterId") int requesterId,
                               @Param("noticeId") int noticeId);

    @Insert("INSERT INTO requester_notice_read(requesterId, noticeId) " +
            "VALUES(#{requesterId}, #{noticeId})")
    void addRequesterReadRecord(@Param("requesterId") int requesterId,
                                @Param("noticeId") int noticeId);

}

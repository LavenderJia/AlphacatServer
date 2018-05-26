package com.alphacat.mapper;

import com.alphacat.pojo.AdminEmailBrief;
import com.alphacat.pojo.Email;
import com.alphacat.pojo.UserEmailBrief;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailMapper {

    @Select("SELECT MAX(id)+1 FROM email")
    Integer getNewId();

    @Insert("INSERT INTO email(id,time, scope, title, content) " +
            "VALUES (#{id},#{time},#{scope},#{title},#{content})")
    void add(Email email);

    @Delete("DELETE FROM email WHERE id = #{id}")
    void delete(int id);

    @Select("SELECT * FROM email WHERE id = #{id}")
    Email get(int id);

    /**
     * Retrieve all emails that are shown to super admin.
     * @see #getAdminEmails(int) to retrieve emails that are shown to normal admin
     */
    @Select("SELECT id, title, time, scope FROM email")
    List<AdminEmailBrief> getAll();

    /**
     * Retrieve emails that are shown to admin except for super admin.
     * It will always return no-scope emails.
     * For super admin's email retrieving, use #getAll()
     * @param scope
     *      0 for no scope, 1 for worker scope, 2 for requester scope
     * @see #getAll() to retrieve emails that are shown to super admin
     */
    @Select("SELECT id, title, time, scope FROM email " +
            "WHERE scope = #{scope} OR scope = 0")
    List<AdminEmailBrief> getAdminEmails(@Param("scope") int scope);

    /**
     * Get emails that are shown to workers.
     */
    @Select("SELECT id, title, time, COUNT(workerId) state " +
            "FROM email e LEFT JOIN (" +
                "SELECT * FROM worker_email_read " +
                "WHERE workerId = #{workerId}" +
            ") r ON id = emailId " +
            "WHERE scope = 0 OR scope = 2 " +
            "GROUP BY id")
    List<UserEmailBrief> getWorkerEmails(@Param("workerId") int workerId);

    /**
     * Get emails that are shown to requesters.
     */
    @Select("SELECT id, title, time, COUNT(requesterId) state " +
            "FROM email e LEFT JOIN (" +
                "SELECT * FROM requester_email_read " +
                "WHERE requesterId = #{requesterId}" +
            ") r ON id = emailId " +
            "WHERE scope = 0 OR scope = 1 " +
            "GROUP BY id")
    List<UserEmailBrief> getRequesterEmails(@Param("requesterId") int requesterId);

    @Insert("INSERT INTO worker_email_read(workerId,emailId) " +
            "VALUES(#{workerId},#{emailId})")
    void addWorkerReadRecord(int workerId, int emailId);

    @Insert("INSERT INTO requester_email_read(requesterId,emailId) " +
            "VALUES(#{requesterId},#{emailId})")
    void addRequesterReadRecord(int requesterId, int emailId);

}

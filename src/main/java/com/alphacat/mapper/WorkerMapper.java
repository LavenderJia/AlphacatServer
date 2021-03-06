package com.alphacat.mapper;

import com.alphacat.pojo.Worker;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkerMapper {

    @Select("SELECT * FROM worker WHERE state=#{state} AND id > 0")
    List<Worker> getByState(@Param("state") int state);

    @Insert("INSERT INTO worker(id, name, birth, sex, email, signature, exp, credit, state, time) " +
            "VALUES(#{id},#{name},#{birth},#{sex},#{email},#{signature},#{exp},#{credit},#{state},CURDATE())")
    void add(Worker worker);

    /**
     * Update a worker's name, birth, sex, email and signature.
     */
    @Update("UPDATE worker SET name=#{name}, birth=#{birth}, sex=#{sex}, email=#{email}, " +
			"signature=#{signature} WHERE id=#{id}")
    void update(Worker worker);

    @Update("UPDATE worker SET state=#{state} WHERE id=#{workerId}")
    void changeState(@Param("workerId") int workerId, @Param("state") int state);

    @Update("UPDATE worker SET password=#{password} WHERE name=#{name}")
    void setPwd(@Param("name") String name, @Param("password") String password);

    @Select("SELECT COUNT(*) FROM worker WHERE name=#{name} AND password=#{password}")
    boolean checkPwd(@Param("name") String name, @Param("password") String password);

    @Select("SELECT COUNT(*) FROM worker WHERE name=#{name}")
    boolean checkName(String name);

    @Update("UPDATE worker SET exp = exp + #{exp} WHERE id=#{workerId}")
    void addExp(@Param("workerId") int workerId, @Param("exp") int exp);

    @Update("UPDATE worker SET credit = credit + #{credit} WHERE id=#{id}")
    void addCredit(@Param("id") int id, @Param("credit") int credit);

    @Select("SELECT MAX(id)+1 FROM worker")
    Integer getNewId();

    @Select("SELECT * FROM worker WHERE id > 0")
    List<Worker> getAll();

    @Select("SELECT * FROM worker WHERE name=#{name}")
    Worker getByName(@Param("name") String name);

	@Select("SELECT * FROM worker WHERE id=#{id}")
	Worker get(@Param("id") int id);

    @Delete("DELETE FROM worker WHERE id=#{workerId}")
    void delete(int workerId);

    @Select("SELECT DISTINCT * FROM worker WHERE state = 0 AND id > 0 " +
            "ORDER BY credit DESC LIMIT #{num}")
    List<Worker> getSortedByCredit(@Param("num") int num);

    @Select("SELECT DISTINCT * FROM worker WHERE state = 0  AND id > 0 " +
            "ORDER BY exp DESC LIMIT #{num}")
    List<Worker> getSortedByExp(@Param("num") int num);

    /**
     * This method cannot verify whether #id is valid.
     * Please verify #id before calling this method.
     * @param id a valid workerId
     */
    @Select("SELECT COUNT(*)+1 FROM worker w1 CROSS JOIN (" +
                "SELECT * FROM worker WHERE id = #{id}" +
            ") w2 WHERE w1.credit > w2.credit AND w1.state = 0")
    int getCreditRank(@Param("id") int id);

    /**
     * This method cannot verify whether #id is valid.
     * Please verify #id before calling this method.
     * @param id a valid workerId
     */
    @Select("SELECT COUNT(*)+1 FROM worker w1 CROSS JOIN worker w2 " +
            "WHERE w1.exp > w2.exp AND w2.id = #{id} AND w1.state = 0")
    int getExpRank(@Param("id") int id);

    @Select("SELECT rectAccuracy FROM worker WHERE id = #{id}")
    double getRectAccuracy(@Param("id") int id);

    @Select("SELECT labelAccuracy FROM worker WHERE id = #{id}")
    double getLabelAccuracy(@Param("id") int id);

    @Update("UPDATE worker SET rectAccuracy = #{ra}, labelAccuracy = #{la} " +
            "WHERE id = #{id}")
    void updateAccuracy(@Param("id") int id, @Param("ra") double rectAccuracy,
                        @Param("la") double labelAccuracy);

    /** 0 represents male*/
    @Select("SELECT COUNT(*) FROM worker WHERE sex = #{sex} AND id > 0")
    Integer getNumBySex(@Param("sex") int sex);

    @Select("SELECT COUNT(*) FROM worker WHERE TIMESTAMPDIFF(year,birth,CURDATE()) >= #{begin} AND " +
            "TIMESTAMPDIFF(year,birth,CURDATE()) < #{end} AND id > 0")
    Integer getNumByAge(@Param("begin") int begin, @Param("end") int end);

    @Select("SELECT COUNT(*) FROM worker WHERE DATEDIFF(#{date}, time) >= 0 AND id > 0")
    Integer getNumUntilDate(@Param("date") String date);

    @Select("SELECT COUNT(*) FROM worker WHERE DATEDIFF(#{date}, time) >= 0 AND id > 0" +
            "AND ((SELECT COUNT(*) FROM squareTag WHERE time=#{date})" +
            "OR (SELECT COUNT(*) FROM irregulartag WHERE time=#{date}))")
    Integer getActiveNum(@Param("date") String date);

}

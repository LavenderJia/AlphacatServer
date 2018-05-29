package com.alphacat.mapper;

import com.alphacat.pojo.Worker;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkerMapper {

    @Select("SELECT * FROM worker WHERE state=#{state}")
    List<Worker> getByState(@Param("state") int state);

    @Insert("INSERT INTO worker(id, name, birth, sex, email, signature, exp, credit, state) " +
            "VALUES(#{id},#{name},#{birth},#{sex},#{email},#{signature},#{exp},#{credit},#{state})")
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

    @Select("SELECT * FROM worker")
    List<Worker> getAll();

    @Select("SELECT * FROM worker WHERE name=#{name}")
    Worker getByName(@Param("name") String name);

	@Select("SELECT * FROM worker WHERE id=#{id}")
	Worker get(@Param("id") int id);

    @Delete("DELETE FROM worker WHERE id=#{workerId}")
    void delete(int workerId);

    @Select("SELECT DISTINCT * FROM worker WHERE state = 0 " +
            "ORDER BY credit DESC LIMIT #{num}")
    List<Worker> getSortedByCredit(@Param("num") int num);

    @Select("SELECT DISTINCT * FROM worker WHERE state = 0 " +
            "ORDER BY exp DESC LIMIT #{num}")
    List<Worker> getSortedByExp(@Param("num") int num);

    /**
     * This method cannot verify whether #id is valid.
     * Please verify #id before calling this method.
     * @param id a valid workerId
     */
    @Select("SELECT COUNT(*)+1 FROM worker w1 CROSS JOIN (" +
                "SELECT * FROM worker WHERE id = #{id}" +
            ") w2 WHERE w1.credit > w2.credit w1.state = 0")
    int getCreditRank(@Param("id") int id);

    /**
     * This method cannot verify whether #id is valid.
     * Please verify #id before calling this method.
     * @param id a valid workerId
     */
    @Select("SELECT COUNT(*)+1 FROM worker w1 CROSS JOIN worker w2 " +
            "WHERE w1.exp > w2.exp AND w2.id = #{id} AND w1.state = 0")
    int getExpRank(@Param("id") int id);

}

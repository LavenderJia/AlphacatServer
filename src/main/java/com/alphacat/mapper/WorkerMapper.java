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

    @Update("UPDATE worker SET name=#{name}, birth=#{birth}, sex=#{sex}, email=#{email}, " +
			"signature=#{signature}, exp=#{exp}, state=#{state} WHERE id=#{id}")
    void update(Worker worker);

    @Update("UPDATE worker SET state=#{state} WHERE id=#{workerId}")
    void changeState(@Param("workerId") int workerId, @Param("state") int state);

    @Update("UPDATE worker SET password=#{password} WHERE name=#{name}")
    void setPwd(@Param("name") String name, @Param("password") String password);

    @Select("SELECT COUNT(*) FROM worker WHERE name=#{name} AND password=#{password}")
    boolean checkPwd(@Param("name") String name, @Param("password") String password);

    @Select("SELECT COUNT(*) FROM worker WHERE name=#{name}")
    boolean checkName(String name);

    @Update("UPDATE worker SET exp=#{exp} WHERE id=#{workerId}")
    void addExp(@Param("workerId") int workerId, @Param("exp") int exp);

    @Select("SELECT MAX(id)+1 FROM worker")
    Integer getNewId();

    @Select("SELECT * FROM worker")
    List<Worker> getAll();

    @Select("SELECT * FROM worker WHERE name=#{name}")
    Worker getByName(@Param("name") String name);

    @Delete("DELETE FROM worker WHERE id=#{workerId}")
    void delete(int workerId);
}

package com.alphacat.mapper;

import com.alphacat.pojo.Requester;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RequesterMapper {

	@Select("SELECT * FROM requester WHERE state=#{state}")
	List<Requester> getByState(@Param("state") int state);

	/**
     * Filter off 0-id requester, because it's system requester.
     */
	@Select("SELECT * FROM requester WHERE id > 0")
	List<Requester> getAll();

	@Select("SELECT * FROM requester WHERE name=#{name}")
	Requester getByName(@Param("name") String name);

	@Select("SELECT * FROM requester WHERE id=#{id}")
	Requester get(@Param("id") int id);

	@Insert("INSERT INTO requester(id, name, birth, sex, email, occupation, company, state) " +
			"VALUES(#{id}, #{name}, #{birth}, #{sex}, #{email}, #{occupation}, #{company}, #{state})")
	void add(Requester r);

	/**
     * This method does NOT change a requester's state.
     * If #r.id not exist, it will do nothing.
     */
	@Update("UPDATE requester SET name=#{name}, birth=#{birth}, sex=#{sex}, email=#{email}, " +
			"occupation=#{occupation}, company=#{company} WHERE id=#{id}")
	void update(Requester r);

	@Delete("DELETE FROM requester WHERE id=#{id}")
	void delete(@Param("id") int id);

	@Update("UPDATE requester SET state=0 WHERE id=#{id}")
	void activate(@Param("id") int id);

	@Update("UPDATE requester SET password=#{pwd} WHERE name=#{name}")
	void setPwd(@Param("name") String name, @Param("pwd") String pwd);

	@Select("SELECT COUNT(*) FROM requester WHERE name=#{name} AND password=#{pwd}")
	boolean checkPwd(@Param("name") String name, @Param("pwd") String pwd);

	@Select("SELECT COUNT(*) FROM requester WHERE name=#{name}")
	boolean checkName(@Param("name") String name);

	@Select("SELECT MAX(id)+1 FROM requester")
	Integer getNewId();

}

package com.alphacat.mapper;

import com.alphacat.pojo.Admin;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * This is for ordinary admin accounts. NOT SUPER ADMIN.
 */
@Repository
public interface AdminMapper {

	@Select("SELECT * FROM admin")
	List<Admin> getAll();

	@Select("SELECT * FROM admin WHERE auth=#{auth}")
	List<Admin> getByAuth(@Param("auth") int auth);

	@Select("SELECT * FROM admin WHERE id=#{id}")
	Admin get(@Param("id") int id);

	@Select("SELECT * FROM admin WHERE name=#{name}")
	Admin getByName(@Param("name") String name);

	/**
     * Add a new admin account without a password.
     * Use <code>setPwd(int, String)</code> to set up its password.
     */
	@Insert("INSERT INTO admin(id, name, actual_name, sex, auth) " +
			"VALUES(#{id}, #{name}, #{actualName}, #{sex}, #{auth})")
	void add(Admin admin);

	/**
	 * Update an admin account except for its password and authority.
     * To change its password, use <code>setPwd(int, String)</code> instead.
     * To change its authority, use <code>setAuth(int, int)</code> instead.
	 */
	@Update("UPDATE admin SET name=#{name}, actual_name=#{actualName}, sex=#{sex} WHERE id=#{id}")
	void update(Admin admin);

	@Delete("DELETE FROM admin WHERE id=#{id}")
	void delete(@Param("id") int id);

	@Update("UPDATE admin SET auth=#{auth} WHERE id=#{id}")
	void setAuth(@Param("id") int id, @Param("auth") int auth);

	@Update("UPDATE admin SET password=#{pwd} WHERE name=#{name}")
	void setPwd(@Param("name") String name, @Param("pwd") String pwd);

	@Select("SELECT COUNT(*) FROM admin WHERE name=#{name} AND password=#{pwd}")
	boolean checkPwd(@Param("name") String name, @Param("pwd") String pwd);

	@Select("SELECT COUNT(*) FROM admin WHERE name=#{name}")
	boolean checkName(@Param("name") String name);

	@Select("SELECT MAX(id)+1 FROM admin")
	Integer getNewId();

}

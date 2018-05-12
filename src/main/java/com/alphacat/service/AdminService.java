package com.alphacat.service;

import com.alphacat.vo.AdminVO;
import java.util.List;

public interface AdminService {

	/**
	 * Get all admin accounts. NOT SUPER ADMIN. 
	 */
	List<AdminVO> getAll();

	/**
	 * Get admin accounts by authority. 
	 */
	List<AdminVO> getByAuth(int auth);

	/**
	 * Get an admin account by its id. 
	 */
	AdminVO get(int id);

	/**
	 * Add an admin account and allocate a new id for it. 
	 */
	void add(AdminVO admin);

	/**
     * Update an admin account except for its password and authority.
     * Use <code>setAuth(int, int)</code> in order to set its authority.
     * Use <code>SecurityService</code> class in order to set up its password.
     */
	void update(AdminVO admin);

	void delete(int id);

	/**
     * Set up the authority of an admin account.
     */
	void setAuth(int id, int auth);

}

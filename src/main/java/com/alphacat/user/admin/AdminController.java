package com.alphacat.user.admin;

import com.alphacat.service.AdminService;
import com.alphacat.service.SecurityService;
import com.alphacat.vo.AdminVO;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private AdminService adminService;
	@Autowired
	private SecurityService securityService;

	@RequestMapping(value="", method=RequestMethod.POST)
	public String addAdmin(@RequestBody JSONObject jo) {
		try{
			String name = jo.getString("name");
			AdminVO admin = new AdminVO();
			admin.setName(name);
			admin.setActualName(jo.getString("actualName"));
			admin.setSex(jo.getIntValue("sex"));
			admin.setAuth(jo.getIntValue("auth"));
			adminService.add(admin);
			String pwd = jo.getString("key");
			securityService.setAdminPassword(name, pwd);
			return null;
		} catch(Exception e) {
			e.printStackTrace();
			return "抱歉，由于未知原因，无法添加该管理员。";
		}
	}

	@RequestMapping(value="", method=RequestMethod.GET)
	public Object getAll() {
		try{
			List<AdminVO> admins = adminService.getAll();
			return admins;
		} catch(Exception e) {
			e.printStackTrace();
			return "抱歉，由于未知原因，无法获取管理员列表。";
		}
	}

	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	public String delete(@PathVariable("id") int id) {
		try{
			adminService.delete(id);
			return null;
		} catch(Exception e) {
			e.printStackTrace();
			return "抱歉，由于未知原因，无法删除该管理员账户。";
		}
	}

	@RequestMapping(value="/{id}", method=RequestMethod.PUT)
	public String update(@RequestBody JSONObject jo, @PathVariable("id") int id) {
		try{
			String name = jo.getString("name");
			String pwd = jo.getString("key");
			securityService.setAdminPassword(name, pwd);
			AdminVO admin = adminService.get(id);
			admin.setId(id);
			admin.setName(name);
			admin.setActualName(jo.getString("actualName"));
			admin.setSex(jo.getIntValue("sex"));
			adminService.update(admin);
			return null;
		} catch(Exception e) {
			e.printStackTrace();
			return "抱歉，由于未知原因，无法更新该管理员账户。";
		}
	}

	@RequestMapping(value="/{id}/setauth", method=RequestMethod.PUT)
	public String setAuth(@PathVariable("id") int id, @RequestBody JSONObject jo) {
		try{
			int auth = jo.getIntValue("auth");
			adminService.setAuth(id, auth);
			return null;
		} catch(Exception e) {
			e.printStackTrace();
			return "抱歉，由于未知原因，无法更改该管理员的权限。";
		}
	}

}

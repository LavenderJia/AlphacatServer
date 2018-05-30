package com.alphacat.user.admin;

import com.alphacat.service.AdminService;
import com.alphacat.service.SecurityService;
import com.alphacat.vo.AdminVO;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private AdminService adminService;
	@Autowired
	private SecurityService securityService;

	@RequestMapping(value="", method=RequestMethod.POST)
	public void addAdmin(@RequestBody JSONObject jo) {
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
		} catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("抱歉，由于未知原因，无法添加该管理员。");
		}
	}

	@RequestMapping(value="", method=RequestMethod.GET)
	@ResponseBody
	public Object getAll() {
		try{
			List<AdminVO> admins = adminService.getAll();
			return admins.stream()
					.filter((admin)->admin.getAuth() != 0)
					.collect(Collectors.toList());
		} catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("抱歉，由于未知原因，无法获取管理员列表。");
		}
	}

	@GetMapping("/{id}")
	public Object get(@PathVariable("id") int id) {
		try{
			return adminService.get(id);
		} catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("抱歉，由于未知原因，无法删除该管理员账户。");
		}
	}

	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	public void delete(@PathVariable("id") int id) {
		try{
			adminService.delete(id);
		} catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("抱歉，由于未知原因，无法删除该管理员账户。");
		}
	}

	@RequestMapping(value="/{id}", method=RequestMethod.PUT)
	public void update(@RequestBody JSONObject jo, @PathVariable("id") int id) {
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
		} catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("抱歉，由于未知原因，无法更新该管理员账户。");
		}
	}

	@RequestMapping(value="/{id}/setauth", method=RequestMethod.PUT)
	public void setAuth(@PathVariable("id") int id, @RequestBody JSONObject jo) {
		try{
			int auth = jo.getIntValue("auth");
			adminService.setAuth(id, auth);
		} catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("抱歉，由于未知原因，无法更改该管理员的权限。");
		}
	}

}

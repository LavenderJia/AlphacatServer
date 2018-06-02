package com.alphacat.user.login;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.session.Session;
import org.springframework.web.bind.annotation.*;
import com.alibaba.fastjson.JSONObject;
@CrossOrigin
@RestController
public class LoginController {

    /**
     * User login and save user's id, name, and type into session.
     * @param jo json object of the following form
	 * {
	 *		name: user name,
	 *		key: password,
	 *		type: user type [requester|worker|admin]
	 * }
     * @return json object of the following form
	 * {
	 *		result: [success|failed|error],
	 *		info: msg return when error occurs,
	 *		id: user id,
	 *		name: user name,
	 *		type: user type [requester|worker|admin]
	 * }
     */
    @ResponseBody
    @RequestMapping("/login")
    public JSONObject loginAction(@RequestBody JSONObject jo) {
		JSONObject result = new JSONObject();
        String type = jo.getString("type");
		try{
			String name = jo.getString("name");
			String pwd = jo.getString("key");
			AuthenticationToken token = new UsernamePasswordToken(type.charAt(0) + name, pwd);
			Subject subject = SecurityUtils.getSubject();
			// this method can save user's id, name, and type as "id", 
			// "name", and "role" into subject's session
			subject.login(token);
			result.put("result", "success");
			Session s = subject.getSession();
			result.put("id", String.valueOf(s.getAttribute("id")));
			result.put("name", String.valueOf(s.getAttribute("name")));
			result.put("type", String.valueOf(s.getAttribute("role")));
		} catch(DisabledAccountException e) {
			result.put("result", "failed");
			if("requester".equals(type)) {
			    result.put("info", "抱歉，该发布者还未被审核通过。");
            } else if("worker".equals(type)) {
			    result.put("info", "抱歉，该工人账户已被管理员禁用。");
            }
		} catch(UnknownAccountException e) {
			result.put("result", "failed");
			result.put("info", "该账户不存在");
		} catch(AccountException e) {
			result.put("result", "failed");
			result.put("info", "密码错误");
		} catch(Exception e) {
			e.printStackTrace();
			result.put("result", "failed");
			result.put("info", "抱歉，由于未知原因，无法登陆。");
		} finally {
			return result;
		}
    }
}

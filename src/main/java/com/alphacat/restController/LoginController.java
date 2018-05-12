package com.alphacat.restController;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.session.Session;
import org.springframework.web.bind.annotation.*;
import com.alibaba.fastjson.JSONObject;

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
    @RequestMapping("/loginAction")
    public JSONObject loginAction(@RequestBody JSONObject jo) {
		JSONObject result = new JSONObject();
		try{
			String name = jo.getString("name");
			String pwd = jo.getString("key");
			String type = jo.getString("type");
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
			result.put("info", e.getMessage());
		} catch(UnknownAccountException e) {
			result.put("result", "failed");
			result.put("info", e.getMessage());
		} catch(AccountException e) {
			result.put("result", "failed");
			result.put("info", e.getMessage());
		} catch(Exception e) {
			System.out.println("\n------------------LoginAction Error--------------------");
			e.printStackTrace();
			System.out.println();
			result.put("result", "error");
			result.put("info", e.getCause().getMessage());
		} finally {
			return result;
		}
    }
}

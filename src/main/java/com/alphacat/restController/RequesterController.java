package com.alphacat.restController;

import com.alphacat.service.RequesterService;
import com.alphacat.service.SecurityService;
import com.alphacat.vo.RequesterVO;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/requester")
public class RequesterController {

    @Autowired
    private RequesterService requesterService;
    @Autowired
    private SecurityService securityService;

    /**
     * Add a not-passed requester and set up its password. 
     */
    @RequestMapping(value="", method=RequestMethod.POST)
	@ResponseBody
    public String addRequester(@RequestBody JSONObject jo) {
		try{
			RequestVO r = new RequesterVO();
			r.setName(jo.getString("name"));
			r.setBirth(jo.getString("birth"));
			r.setSex(jo.getIntValue("sex"));
			r.setEmail(jo.getString("email"));
			r.setOccupation("occupation");
			r.setCompany("company");
			r.setState(1);
			requesterService.addRequester(requester);
			securityService.setRequesterPassword(r.getName(), jo.getString("password"));
			return null;
		} catch(Exception e) {
			e.printStackTrace();
			return e.getSource().getMessage();
		}
    }

	/**
	 * Get a requester by id.
	 */
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	@ResponseBody
	public RequesterVO get(@PathVariable("id") int id) {
		return requesterService.get(id);
	}

    /**
     * Update a requester. 
     */
    @RequestMapping(value="/{id}", method=RequestMethod.PUT)
	@ResponseBody
    public String updateRequester(@RequestBody JSONObject jo, @PathVariable("id") int id) {
		try{
			RequesterVO original = requesterService.get(id);
			if(original.getState() == 1) {
				System.out.print("-------------------------------");
				System.out.print("Warning in Requester Controller's updateRequester(..): Requester is not active. ");
				System.out.print("Inactive requester not processed.");
				System.out.println("-------------------------------");
				return "Requester " + id + " is inactive.";
			}
			String name = jo.getString("name");
			RequesterVO r = new RequesterVO();
			r.setId(id);
			r.setName(name);
			r.setBirth(jo.getString("birth"));
			r.setSex(jo.getIntValue("sex"));
			r.setEmail(jo.getString("email"));
			r.setOccupation(jo.getString("occupation"));
			r.setCompany(jo.getString("company"));
			r.setState(0);
			requesterService.updateRequester(r);
			securityService.setRequesterPassword(name, jo.getString("key"));
			return null;
		} catch(Exception e) {
			e.printStackTrace();
			return e.getSource().getMessage();
		}
    }

    /**
     * Pass or unpass a requester. 
     */
    @RequestMapping(value="/check", method=RequestMethod.POST)
    public String checkRequester(@RequestBody JSONObject jo) {
		try{
			int id = jo.getIntValue("id");
			int isChecked = jo.getBooleanValue("isChecked");
			requesterService.checkRequester(id, isChecked);
			return null;
		} catch(Exception e) {
			e.printStackTrace();
			return e.getSource().getMessage();
		}
    }

	/**
	 * Get an array of requesters of a certain state. 
	 * @param state [all|checked|unchecked]
	 */
	public JSONArray get(@RequestParam("state") String state) {
		JSONArray result = new JSONArray();
		int stateInt;
		if(state.equals("all")){
			stateInt = -1;
		} else if(state.equals("checked")) {
			stateInt = 0;
		} else if(state.equals("unchecked")) {
			stateInt = 1;
		} else {
			System.out.print("-----------------------------");
			System.out.print("State " + state + " cannot be processed. Aborted.");
			System.out.println("-----------------------------");
			return result;
		}
		List<RequesterVO> rs = requesterService.getByState(stateInt);
		if(stateInt == -1) {
			result.addAll(rs);
		} else {
			rs.forEach(e -> {
				JSONObject r = new JSONObject();
				r.put("id", e.getId());
				r.put("name", e.getName());
				r.put("birth", e.getBirth());
				r.put("sex", e.getSex());
				r.put("email", e.getEmail());
				r.put("occupation", e.getOccupation());
				r.put("company", e.getCompany());
				result.add(r);
			});
		}
		return result;
	}

    /**
     * 检查是否有同名的昵称
     * @param name 昵称
     * @return true=有
     */
    @RequestMapping("/checkName")
    public boolean checkName(@ModelAttribute("name")String name) {
        return requesterService.hasSameName(name);
    }

    /**
     * 以name为昵称的用户有权限修改自己的密码
     * @param name 昵称
     * @param password 设定的密码
	 * @Deprecated Use updateRequester(..) instead.
     */
    @RequestMapping("/{name}/setPassword")
    public void setPassword(@PathVariable("name") String name, @ModelAttribute("password") String password) {
        securityService.setRequesterPassword(name, password);
    }
}

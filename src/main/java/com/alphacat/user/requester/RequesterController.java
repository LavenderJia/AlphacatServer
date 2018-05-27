package com.alphacat.user.requester;

import com.alphacat.service.RequesterService;
import com.alphacat.service.SecurityService;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alphacat.vo.RequesterVO;
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
    public void addRequester(@RequestBody JSONObject jo) {
		try{
			RequesterVO r = new RequesterVO();
			r.setName(jo.getString("name"));
			r.setBirth(jo.getString("birth"));
			r.setSex(jo.getIntValue("sex"));
			r.setEmail(jo.getString("notice"));
			r.setOccupation(jo.getString("occupation"));
			r.setCompany(jo.getString("company"));
			r.setState(1);
			requesterService.addRequester(r);
			securityService.setRequesterPassword(r.getName(), jo.getString("key"));
		} catch(Exception e) {
		    e.printStackTrace();
			throw new RuntimeException("抱歉，无法添加发布者。");
		}
    }

	/**
	 * Get a requester by id.
	 */
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	@ResponseBody
	public Object get(@PathVariable("id") int id) {
		try{
			return requesterService.get(id);
		} catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("抱歉，无法获取发布者信息。");
		}
	}

    /**
     * Update a requester. 
	 * Cannot update one that has not been passed by admin. 
     */
    @RequestMapping(value="/{id}", method=RequestMethod.PUT)
    public void updateRequester(@RequestBody JSONObject jo, @PathVariable("id") int id) {
		try{
			// Check for state. Only passed account can update info.
			RequesterVO original = requesterService.get(id);
			if(original.getState() == 1) {
				System.out.println("Inactive requester not processed.");
				throw new RuntimeException("该发布者账户还未被管理员审核。");
			}
			String name = jo.getString("name");
			RequesterVO r = new RequesterVO();
			r.setId(id);
			r.setName(name);
			r.setBirth(jo.getString("birth"));
			r.setSex(jo.getIntValue("sex"));
			r.setEmail(jo.getString("notice"));
			r.setOccupation(jo.getString("occupation"));
			r.setCompany(jo.getString("company"));
			r.setState(0);
			requesterService.updateRequester(r);
			securityService.setRequesterPassword(name, jo.getString("key"));
		} catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("抱歉，无法更新发布者信息。");
		}
    }

    /**
     * Pass or unpass a requester. 
     */
    @RequestMapping(value="/check", method=RequestMethod.POST)
    public void checkRequester(@RequestBody JSONObject jo) {
		try{
			int id = jo.getIntValue("id");
			boolean isChecked = jo.getBooleanValue("isChecked");
			requesterService.checkRequester(id, isChecked);
		} catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("抱歉，无法通过该发布者。");
		}
    }

	/**
	 * Get an array of requesters of a certain state. 
	 * @param state [all|checked|unchecked]
	 */
	@RequestMapping(value="", method=RequestMethod.GET)
    @ResponseBody
	public Object get(@RequestParam("state") String state) {
		try{
			JSONArray result = new JSONArray();
			int stateInt;
			if(state.equals("all")){
				stateInt = -1;
			} else if(state.equals("checked")) {
				stateInt = 0;
			} else if(state.equals("unchecked")) {
				stateInt = 1;
			} else {
			    throw new RuntimeException("无法识别该发起者状态：" + state);
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
					r.put("notice", e.getEmail());
					r.put("occupation", e.getOccupation());
					r.put("company", e.getCompany());
					result.add(r);
				});
			}
			return result;
		} catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("抱歉，无法获取请求的发布者列表。");
		}
	}

    /**
     * 检查是否有同名的昵称
     * @param name 昵称
     * @return true=有
     */
    @RequestMapping("/checkName")
    @ResponseBody
    public boolean checkName(@ModelAttribute("name")String name) {
        try {
            return requesterService.hasSameName(name);
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("抱歉，由于未知原因，无法查询发布者昵称。");
        }
    }

    /**
     * 以name为昵称的用户有权限修改自己的密码
     * @param name 昵称
     * @param password 设定的密码
	 * @deprecated Use updateRequester(..) instead.
     */
    @RequestMapping("/{name}/setPassword")
    public void setPassword(@PathVariable("name") String name, @ModelAttribute("password") String password) {
        try{
            securityService.setRequesterPassword(name, password);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("抱歉，由于未知原因，无法设置密码。");
        }
    }
}

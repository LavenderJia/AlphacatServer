package com.alphacat.restController;

import com.alphacat.service.SecurityService;
import com.alphacat.service.WorkerService;
import com.alphacat.vo.WorkerVO;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/worker")
public class WorkerController {

    @Autowired
    private WorkerService workerService;
    @Autowired
    private SecurityService securityService;

    @RequestMapping(value="", method=RequestMethod.POST)
    public String addWorker(@RequestBody JSONObject jo) {
		try{
			WorkerVO w = new WorkerVO();
			w.setName(jo.getString("name"));
			w.setBirth(jo.getString("birth"));
			w.setSex(jo.getIntValue("sex"));
			w.setEmail(jo.getString("email"));
			w.setSignature(jo.getString("signatrue"));
			workerService.addWorker(w);
			securityService.setWorkerPassword(w.getName(), jo.getString("key"));
			return null;
		} catch(Exception e) {
			e.printStackTrace();
			return "抱歉，添加工人账户失败。";
		}
    }

	/**
	 * @param type [all|active|locked]
	 */
    @RequestMapping(value="", method=RequestMethod.GET)
    public Object getByState(@RequestParam("type") String type) {
		try{
			int state;
			if(type.equals("all")) {
				state = -1;
			} else if(type.equals("active")) {
				state = 0;
			} else if(type.equals("locked")) {
				state = 1;
			} else {
				System.out.print("--------------------");
				System.out.print("Type " + type + " cannot been processed.");
				System.out.println("---------------------");
				return "抱歉，不存在该状态的工人账户。";
			}
			List<WorkerVO> ws = workerService.getByState(state);
			JSONArray result = new JSONArray();
			if(state == -1) {
				result.addAll(ws);
			} else {
				ws.forEach(e->{
					JSONObject w = new JSONObject();
					w.put("id", e.getId());
					w.put("name", e.getName());
					w.put("birth", e.getBirth());
					w.put("sex", e.getSex());
					w.put("email", e.getEmail());
					w.put("signature", e.getSignature());
					w.put("exp", e.getExp());
					w.put("credit", e.getCredit());
					result.add(w);
				});
			}
			return result;
		} catch(Exception e) {
			e.printStackTrace();
			return "抱歉，无法查找相关工人账户。";
		}
    }

	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public Object get(@PathVariable("id") int id) {
		try{
			return workerService.get(id);
		} catch(Exception e) {
			e.printStackTrace();
			return "抱歉，无法获取工人信息。";
		}
	}

	/**
	 * Update a worker account. Only active account can be updated. 
	 */
    @RequestMapping(value="/{id}", method=RequestMethod.PUT)
    public String updateWorker(@RequestBody JSONObject jo, @PathVariable("id") int id) {
		try{
			// check the state
			WorkerVO original = workerService.get(id);
			if(original.getState() == 1) {
				System.out.println("Inactive worker not processed.");
				return "该工人账户已被管理员封禁，无法更新。";
			}
			String name = jo.getString("name");
			WorkerVO w = new WorkerVO();
			w.setId(id);
			w.setName(name);
			w.setBirth(jo.getString("birth"));
			w.setSex(jo.getIntValue("sex"));
			w.setEmail(jo.getString("email"));
			w.setSignature(jo.getString("signature"));
			w.setExp(jo.getIntValue("exp"));
			w.setCredit(jo.getIntValue("credit"));
			w.setState(0);
			workerService.updateWorker(w);
			securityService.setWorkerPassword(name, jo.getString("key"));
			return null;
		} catch(Exception e) {
			e.printStackTrace();
			return "抱歉，无法更新该工人账户。";
		}
    }

    @RequestMapping(value="/lock/{id}", method=RequestMethod.POST)
    public String lockWorker(@PathVariable("id") int id) {
		try{
			workerService.setWorkerState(id, true);
			return null;
		} catch(Exception e) {
			e.printStackTrace();
			return "抱歉，由于未知错误，无法封禁该工人账户。";
		}
    }

    @RequestMapping(value="/unlock/{id}", method=RequestMethod.POST)
    public String unlockWorker(@PathVariable("id") int id) {
		try{
			workerService.setWorkerState(id, false);
			return null;
		} catch(Exception e) {
			e.printStackTrace();
			return "抱歉，由于未知错误，无法解禁该工人账户。";
		}
    }

    /**
     * 获取工人排行，按照积分大小，最多取前十名
     * @return 经过排序的结果
     */
    @RequestMapping("/getSorted")
    public List<WorkerVO> getSortedWorkers() {
        return workerService.getSortedWorkers();
    }

    @RequestMapping("/getWorker")
    public WorkerVO getWorker(@ModelAttribute("name") String name) {
        return workerService.getWorkerByName(name);
    }

    /**
     * 以name为昵称的工人有权限修改自己的密码
     * @param name 昵称
     * @param password 设定的密码
	 * @deprecated Use updateWorker(..) instead.
     */
    @RequestMapping("/{name}/setPassword")
    public void setPassword(@PathVariable("name") String name, @ModelAttribute("password") String password) {
        securityService.setWorkerPassword(name, password);
    }

    /**
     * 检查是否有同名的昵称
     * @param name 昵称
     * @return true=有
     */
    @RequestMapping("/checkName")
    public boolean checkName(@ModelAttribute("name")String name) {
        return workerService.hasSameName(name);
    }

    @RequestMapping("/sign")
    public void signIn() {
        int id = (int) SecurityUtils.getSubject().getSession().getAttribute("id");
        workerService.signIn(id);
    }

    @RequestMapping("/signDays")
    public int getSignDays() {
        int id = (int) SecurityUtils.getSubject().getSession().getAttribute("id");
        return workerService.getSignDays(id);
    }

    @RequestMapping("/hasSign")
    public boolean hasSignIn() {
        int id = (int) SecurityUtils.getSubject().getSession().getAttribute("id");
        return workerService.hasSigned(id);
    }
}

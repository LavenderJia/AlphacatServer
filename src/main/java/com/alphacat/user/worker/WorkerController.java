package com.alphacat.user.worker;

import com.alibaba.fastjson.JSON;
import com.alphacat.service.SecurityService;
import com.alphacat.service.WorkerService;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alphacat.vo.WorkerVO;
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
    public void addWorker(@RequestBody JSONObject jo) {
		try{
			WorkerVO w = new WorkerVO();
			w.setName(jo.getString("name"));
			w.setBirth(jo.getString("birth"));
			w.setSex(jo.getIntValue("sex"));
			w.setEmail(jo.getString("email"));
			w.setSignature(jo.getString("signature"));
			workerService.addWorker(w);
			securityService.setWorkerPassword(w.getName(), jo.getString("key"));
		} catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("抱歉，添加工人账户失败。");
		}
    }

	/**
	 * @param type [all|active|locked]
	 */
    @RequestMapping(value="", method=RequestMethod.GET)
    @ResponseBody
    public Object getByState(@RequestParam("type") String type) {
		try{
			int state;
			if("all".equals(type)) {
				state = -1;
			} else if("active".equals(type)) {
				state = 0;
			} else if("locked".equals(type)) {
				state = 1;
			} else {
				throw new RuntimeException("抱歉，不存在该状态的工人账户。");
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
			throw new RuntimeException("抱歉，无法查找相关工人账户。");
		}
    }

	@RequestMapping(value="/{id}", method=RequestMethod.GET)
    @ResponseBody
	public Object get(@PathVariable("id") int id) {
		try{
			return workerService.get(id);
		} catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("抱歉，无法获取工人信息。");
		}
	}

	/**
	 * Update a worker account. Only active account can be updated.
     * Cannot update its exp and credit.
	 */
    @RequestMapping(value="/{id}", method=RequestMethod.PUT)
    public void updateWorker(@RequestBody JSONObject jo, @PathVariable("id") int id) {
		try{
			// check the state
			WorkerVO original = workerService.get(id);
			if(original.getState() == 1) {
				throw new RuntimeException("该工人账户已被管理员封禁，无法更新。");
			}
			String name = jo.getString("name");
			WorkerVO w = new WorkerVO();
			w.setId(id);
			w.setName(name);
			w.setBirth(jo.getString("birth"));
			w.setSex(jo.getIntValue("sex"));
			w.setEmail(jo.getString("email"));
			w.setSignature(jo.getString("signature"));
			w.setState(0);
			workerService.updateWorker(w);
			securityService.setWorkerPassword(name, jo.getString("key"));
		} catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("抱歉，无法更新该工人账户。");
		}
    }

    @RequestMapping(value="/lock/{id}", method=RequestMethod.POST)
    public void lockWorker(@PathVariable("id") int id) {
		try{
			workerService.setWorkerState(id, true);
		} catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("抱歉，由于未知错误，无法封禁该工人账户。");
		}
    }

    @RequestMapping(value="/unlock/{id}", method=RequestMethod.POST)
    public void unlockWorker(@PathVariable("id") int id) {
		try{
			workerService.setWorkerState(id, false);
		} catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("抱歉，由于未知错误，无法解禁该工人账户。");
		}
    }

    /**
     * Get the first 10 workers in order of <code>by</code>.
     * @param by the order by which workers are sorted
     * @return an array of json object in the form of {
     *     id, name, birth, sex, email, signature,
     *     exp(if #by is exp), credit(if #by is credit)
     * }
     */
    @RequestMapping(value = "/data/rank", method = RequestMethod.GET)
    @ResponseBody
    public Object getSortedWorkers(@RequestParam("sorted") String by) {
        try {
            List<WorkerVO> workers = null;
            String removeKey = "";
            if("exp".equals(by)) {
                workers = workerService.getSortedByExp(10);
                removeKey = "credit";
            }
            if ("credit".equals(by)) {
                workers = workerService.getSortedByCredit(10);
                removeKey = "exp";
            }
            // to fit the response pattern
            JSONArray result = new JSONArray();
            if(workers != null) for(WorkerVO w: workers) {
                JSONObject jo = JSON.parseObject(JSON.toJSONString(w));
                jo.fluentRemove(removeKey).fluentRemove("state");
                result.add(jo);
            }
            return result;
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("抱歉，由于未知原因，无法获取排行。");
        }
    }

    @RequestMapping("/getWorker")
    @ResponseBody
    public WorkerVO getWorker(@ModelAttribute("name") String name) {
        try {
            return workerService.getWorkerByName(name);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("抱歉，由于未知原因，无法获取该工人。");
        }
    }

    /**
     * 以name为昵称的工人有权限修改自己的密码
     * @param name 昵称
     * @param password 设定的密码
	 * @deprecated Use updateWorker(..) instead.
     */
    @RequestMapping("/{name}/setPassword")
    public void setPassword(@PathVariable("name") String name, @ModelAttribute("password") String password) {
        try{
            securityService.setWorkerPassword(name, password);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("抱歉，由于未知原因，无法设置该工人的密码。");
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
            return workerService.hasSameName(name);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("抱歉，无法检查昵称。");
        }
    }

    /**
     * Sign up can gain the worker 10 exp.
     * @see WorkerService#signUp(int)
     */
    @RequestMapping(value="/{id}/signup", method = RequestMethod.POST)
    public void signUp(@PathVariable("id") int id) {
        try {
            workerService.signUp(id);
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("抱歉，由于未知原因，无法签到。");
        }
    }

    @RequestMapping(value="/{id}/signup", method = RequestMethod.GET)
    @ResponseBody
    public Object getSignUpInfo(@PathVariable("id") int id) {
        try{
            int days = workerService.getSignDays(id);
            boolean hasSign = workerService.hasSigned(id);
            JSONObject response = new JSONObject();
            response.fluentPut("daysNum", days).fluentPut("hasSign", hasSign);
            return response;
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("抱歉，无法获取用户的签到信息。");
        }
    }

    @RequestMapping(value = "/{id}/rankData", method = RequestMethod.GET)
    public Object getRank(@PathVariable("id") int id) {
        try{
            JSONObject result = new JSONObject();
            int creditRank = workerService.getCreditRank(id);
            if(creditRank == -1) {
                throw new WorkerNotFoundException(id);
            }
            if(creditRank == 0) {
                throw new WorkerBannedException(id);
            }
            int expRank = workerService.getExpRank(id);
            if(expRank == -1) {
                throw new WorkerNotFoundException(id);
            }
            if(expRank == 0) {
                throw new WorkerBannedException(id);
            }
            result.fluentPut("exp", expRank).fluentPut("credit", creditRank);
            return result;
        } catch(WorkerNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("抱歉，该用户不存在。");
        } catch (WorkerBannedException e) {
            e.printStackTrace();
            throw new RuntimeException("抱歉，该用户已被禁用。");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("抱歉，由于未知原因，无法获取该用户的排名信息。");
        }
    }

    private class WorkerNotFoundException extends Exception {
        WorkerNotFoundException(int id) {
            super("Worker not found: " + id);
        }
    }

    private class WorkerBannedException extends Exception {
        WorkerBannedException(int id) {
            super("Worker has been banned: " + id);
        }
    }

}

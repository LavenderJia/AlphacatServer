package com.alphacat.task;

import com.alphacat.service.TaskService;
import com.alphacat.vo.TaskVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/task")
@RestController
public class TaskController {

    @Autowired
    private TaskService taskService;

    @RequestMapping(value="", method= RequestMethod.POST)
    public String add(TaskVO taskVO) {
        try{
            taskService.add(taskVO);
            return null;
        } catch(Exception e) {
            e.printStackTrace();
            return "抱歉，由于未知原因，无法新建该任务。";
        }
    }

    @RequestMapping(value="/{id}", method=RequestMethod.PUT)
    public String update(TaskVO taskVO, @PathVariable("id") int id) {
        try{
            taskVO.setId(id);
            taskService.update(taskVO);
            return null;
        } catch(Exception e) {
            e.printStackTrace();
            return "抱歉，由于未知原因，无法更新该任务。";
        }
    }

    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    public Object get(@PathVariable("id") int id) {
        try{
            return taskService.get(id);
        } catch(Exception e) {
            e.printStackTrace();
            return "抱歉，由于未知原因，无法找到该任务。";
        }
    }

    @RequestMapping(value="/task", method=RequestMethod.GET)
    public Object get(@RequestParam("requesterId") Integer requesterId,
                      @RequestParam("workerId") Integer workerId,
                      @RequestParam("type") String type) {
        if(requesterId != null) {
            return getR(requesterId, type);
        }
        if(workerId != null) {
            return getW(workerId, type);
        }
        return "发布者和工人的编号均无法接收，无法回应。";
    }

    public Object getR(int requesterId, String type) {
        try {
            if ("notstart".equals(type)) {
                return taskService.getIdle(requesterId);
            } else if ("underway".equals(type)) {
                return taskService.getUnderway(requesterId);
            } else if ("history".equals(type)) {
                return taskService.getEnded(requesterId);
            }
        } catch(Exception e) {
            e.printStackTrace();
            return "抱歉，由于未知原因，无法获取发布者的任务列表。";
        }
        return "不支持的任务类型：" + type;
    }

    public Object getW(int id, String type) {
        try{
            if("available".equals(type)) {
                return taskService.getAvailable(id);
            } else if("history".equals(type)) {
                return taskService.getHistory(id);
            }
        } catch(Exception e) {
            e.printStackTrace();
            return "抱歉，由于未知原因，无法获取相关工人任务。";
        }
        return "不支持的任务类型：" + type;
    }

}

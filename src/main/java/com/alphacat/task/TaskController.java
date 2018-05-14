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

    @RequestMapping(value="", method=RequestMethod.GET)
    public Object getR(@RequestParam("requesterId") int id,
                       @RequestParam("type") String type) {
        try {
            if ("notstart".equals(type)) {
                return taskService.getIdle(id);
            } else if ("underway".equals(type)) {
                return taskService.getUnderway(id);
            } else if ("history".equals(type)) {
                return taskService.getEnded(id);
            }
        } catch(Exception e) {
            e.printStackTrace();
            return "抱歉，由于未知原因，无法获取发布者的任务列表。";
        }
        return null;
    }

    @RequestMapping(value="", method=RequestMethod.GET)
    public Object getW(@RequestParam("workerId") int id,
                       @RequestParam("type") String type) {
        // TODO
        return null;
    }

}

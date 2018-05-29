package com.alphacat.task;

import com.alphacat.service.PictureService;
import com.alphacat.service.TaskService;
import com.alphacat.vo.TaskVO;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/task")
@RestController
public class TaskController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private PictureService pictureService;

    @RequestMapping(value="", method= RequestMethod.POST)
    public void add(@RequestBody TaskVO taskVO) {
        try{
            taskService.add(taskVO);
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("抱歉，由于未知原因，无法新建该任务。");
        }
    }

    @RequestMapping(value="/{id}", method=RequestMethod.PUT)
    public void update(@RequestBody TaskVO taskVO, @PathVariable("id") int id) {
        try{
            taskVO.setId(id);
            taskService.update(taskVO);
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("抱歉，由于未知原因，无法更新该任务。");
        }
    }

    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    @ResponseBody
    public Object get(@PathVariable("id") int id) {
        try{
            return taskService.get(id);
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("抱歉，由于未知原因，无法找到该任务。");
        }
    }

    @RequestMapping(value="", method=RequestMethod.GET)
    @ResponseBody
    public Object get(@RequestParam(value = "requesterId", required = false) Integer requesterId,
                      @RequestParam(value = "workerId", required = false) Integer workerId,
                      @RequestParam("type") String type) {
        if(requesterId != null) {
            return getR(requesterId, type);
        }
        if(workerId != null) {
            return getW(workerId, type);
        }
        throw new RuntimeException("发布者和工人的编号均无法接收，无法回应。");
    }

    public Object getR(int requesterId, String type) {
        try {
            if ("notstart".equals(type)) {
                return taskService.getIdle(requesterId);
            } else if ("underway".equals(type)) {
                return taskService.getUnderway(requesterId);
            } else if ("history".equals(type)) {
                return taskService.getEnded(requesterId);
            } else if("all".equals(type)) {
                return taskService.getRequesterTasks(requesterId);
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
            } else if("doing".equals(type)) {
                return taskService.getPartaking(id);
            }else if("history".equals(type)) {
                return taskService.getHistory(id);
            }
        } catch(Exception e) {
            e.printStackTrace();
            return "抱歉，由于未知原因，无法获取相关工人任务。";
        }
        return "不支持的任务类型：" + type;
    }

    @RequestMapping(value="/{id}/picOrder", method = RequestMethod.GET)
    @ResponseBody
    public Object getPicOrder(@PathVariable("id") int id) {
        try {
            int workerId = (Integer) SecurityUtils.getSubject()
                    .getSession().getAttribute("id");
            return pictureService.getPicOrder(id, workerId);
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("抱歉，由于未知原因，无法获取图片顺序。");
        }
    }

}

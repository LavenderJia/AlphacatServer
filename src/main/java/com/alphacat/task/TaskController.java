package com.alphacat.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alphacat.service.PictureService;
import com.alphacat.service.TaskService;
import com.alphacat.service.WorkerService;
import com.alphacat.vo.LabelVO;
import com.alphacat.vo.TaskVO;
import com.alphacat.vo.WorkerVO;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@RequestMapping("/task")
@RestController
public class TaskController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private PictureService pictureService;
    @Autowired
    private WorkerService workerService;

    @PostMapping("")
    public String add(@RequestBody JSONObject request) {
        try{
            //JSONObject info = request.getJSONObject("newTask");
            boolean normal = !"draft".equals(request.getString("state"));
            request.fluentRemove("state");
            //TaskVO taskVO = new TaskVO();
            //taskVO.setRequesterId(request.getInteger("requesterId"));
            //taskVO.setLabels(request.getJSONArray("labels").toJavaList(LabelVO.class));
            TaskVO taskVO = JSON.parseObject(request.toString(), TaskVO.class);
            int id = taskService.add(taskVO, normal);
            //List<MultipartFile> files = JSON.parseArray(request.get("formData").toString(), MultipartFile.class);
            //for(int i = 0; i < files.size(); i++) {
            //    pictureService.uploadPic(files.get(i), id, i);
            //}
            return "{\"taskId\":"+ id + "}";
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("抱歉，由于未知原因，无法新建该任务。");
        }
    }

    @PutMapping("")
    public void update(@RequestBody JSONObject info) {
        try{
            boolean normal = !"draft".equals(info.getString("state"));
            info.fluentRemove("state");
            TaskVO taskVO = JSON.parseObject(info.toString(), TaskVO.class);
            taskService.update(taskVO, normal);
            //pictureService.delete(taskVO.getId());
            //List<MultipartFile> files = JSON.parseArray(request.get("formData").toString(), MultipartFile.class);
            //for(int i = 0; i < files.size(); i++) {
            //    pictureService.uploadPic(files.get(i), id, i);
            //}
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("抱歉，由于未知原因，无法更新该任务。");
        }
    }

    @PostMapping("/{id}/draft")
    public void toDraft(@PathVariable("id") int id) {
        try{
            taskService.setToDraft(id);
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("抱歉，由于未知原因，无法更新该任务。");
        }
    }

    @PostMapping("/{id}/garbage")
    public void toGarbage(@PathVariable("id") int id) {
        try{
            taskService.setToGarbage(id);
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("抱歉，由于未知原因，无法更新该任务。");
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") int id) {
        try{
            taskService.delete(id);
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("抱歉，由于未知原因，无法删除该任务。");
        }
    }
    @GetMapping("/{id}")
    @ResponseBody
    public Object get(@PathVariable("id") int id) {
        try{
            return taskService.get(id);
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("抱歉，由于未知原因，无法找到该任务。");
        }
    }

    @RequestMapping(value = "/{id}/requester", method = RequestMethod.GET)
    @ResponseBody
    public Object getRequesterTask(@PathVariable("id") int id) {
        try{
            TaskVO task = taskService.get(id);
            JSONObject result = JSON.parseObject(JSON.toJSONString(task));
            result.fluentPut("state", taskService.getState(id))
                    .fluentPut("taskProgressRate", taskService.getProgress(id))
                    .fluentPut("workerCount", taskService.getWorkerCount(id))
                    .fluentPut("tagRate", taskService.getTagRate(id))
                    .fluentPut("picUndone", taskService.getPicUndone(id))
                    .fluentPut("costCredit", taskService.getCostCredit(id));
            return result;
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("抱歉，由于未知原因，无法获取任务信息。");
        }
    }

    @RequestMapping(value="", method=RequestMethod.GET)
    @ResponseBody
    public Object get(@RequestParam(value = "requesterId", required = false) Integer requesterId,
                      @RequestParam(value = "workerId", required = false) Integer workerId,
                      @RequestParam(value = "type", required = false) String type) {
        if(requesterId != null) {
            return getR(requesterId, type);
        }
        if(workerId != null) {
            return getW(workerId, type);
        }
        throw new RuntimeException("发布者和工人的编号均无法接收，无法回应。");
    }

    private Object getR(int requesterId, String type) {
        try {
            if ("notstart".equals(type)) {
                return taskService.getIdle(requesterId);
            } else if ("underway".equals(type)) {
                return taskService.getUnderway(requesterId);
            } else if ("history".equals(type)) {
                return taskService.getEnded(requesterId);
            } else if("all".equals(type)) {
                return taskService.getRequesterTasks(requesterId);
            } else if("draft".equals(type)) {
                return taskService.getDraft(requesterId);
            } else if("garbage".equals(type)) {
                return taskService.getGarbage(requesterId);
            }
        } catch(Exception e) {
            e.printStackTrace();
            return "抱歉，由于未知原因，无法获取发布者的任务列表。";
        }
        return "不支持的任务类型：" + type;
    }

    private Object getW(int id, String type) {
        try{
            WorkerVO worker = workerService.get(id);
            if (worker.getState() == 2)  {
                return taskService.getTestTasks(id);
            }
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

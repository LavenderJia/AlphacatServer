package com.alphacat.task.test;

import com.alibaba.fastjson.JSONObject;
import com.alphacat.service.TestTaskService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/test")
@RestController
public class TestTaskController {

    private TestTaskService testTaskService;

    @Autowired
    public TestTaskController(TestTaskService testTaskService) {
        this.testTaskService = testTaskService;
    }

    @RequestMapping("/{taskId}")
    @ResponseBody
    public Object testAnswer(@PathVariable("taskId") int taskId) {
        try{
            Session session = SecurityUtils.getSubject().getSession();
            String role = String.valueOf(session.getAttribute("role"));
            JSONObject result = new JSONObject();
            if(role.equals("worker")) {
                int workerId = (Integer) session.getAttribute("id");
                boolean success = testTaskService.test(taskId, workerId);
                if(success) {
                    result.put("result", "success");
                    testTaskService.testAllFinished(workerId);
                } else {
                    result.put("result", "failed");
                }
            } else {
                result.put("result", "not a worker");
            }
            return result;
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("抱歉，由于未知原因，无法测试结果。");
        }
    }

}

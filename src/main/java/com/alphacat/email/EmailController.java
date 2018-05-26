package com.alphacat.email;

import com.alibaba.fastjson.JSONObject;
import com.alphacat.service.EmailService;
import com.alphacat.vo.EmailVO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

@RestController
@RequestMapping("/notice")
public class EmailController {

    private EmailService service;

    @Autowired
    public EmailController(EmailService service) {
        this.service = service;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public Object getNoticeList() {
        try{
            Session session = SecurityUtils.getSubject().getSession();
            String role = session.getAttribute("role").toString();
            if("worker".equals(role)) {
                int id = (Integer) session.getAttribute("id");
                return service.getWorkerEmailList(id);
            }
            if("requester".equals(role)) {
                int id = (Integer) session.getAttribute("id");
                return service.getRequesterEmailList(id);
            }
            if("superAdmin".equals(role)) {
                return service.getAdminEmailList(0);
            }
            if("requesterAdmin".equals(role)) {
                return service.getAdminEmailList(1);
            }
            if("workerAdmin".equals(role)) {
                return service.getAdminEmailList(2);
            }
            throw new RuntimeException("Cannot resolve user's role" + role);
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("抱歉，由于未知原因，无法获取通知列表。");
        }
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public void add(@RequestBody  JSONObject jo) {
        try {
            String title = jo.getString("title");
            String content = jo.getString("content");
            int type = jo.getIntValue("type");
            EmailVO email = new EmailVO();
            email.setTitle(title);
            email.setContent(content);
            email.setType(type);
            service.add(email);
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("抱歉，由于未知原因，无法添加通知。");
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") int id) {
        try{
            service.delete(id);
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("抱歉，由于未知原因，无法删除通知。");
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Object get(@PathVariable("id") int id) {
        try{
            return service.get(id);
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("抱歉，由于未知原因，无法得到该通知。");
        }
    }

}

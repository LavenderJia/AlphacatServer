package com.alphacat.notice;

import com.alibaba.fastjson.JSONObject;
import com.alphacat.service.NoticeService;
import com.alphacat.vo.NoticeVO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notice")
public class NoticeController {

    private NoticeService service;

    @Autowired
    public NoticeController(NoticeService service) {
        this.service = service;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public Object getNoticeList() {
        try{
            Session session = SecurityUtils.getSubject().getSession();
            String role = String.valueOf(session.getAttribute("role"));
            if("worker".equals(role)) {
                int id = (Integer) session.getAttribute("id");
                return service.getWorkerNoticeList(id);
            }
            if("requester".equals(role)) {
                int id = (Integer) session.getAttribute("id");
                return service.getRequesterNoticeList(id);
            }
            if("superAdmin".equals(role)) {
                return service.getAdminNoticeList(0);
            }
            if("requesterAdmin".equals(role)) {
                return service.getAdminNoticeList(1);
            }
            if("workerAdmin".equals(role)) {
                return service.getAdminNoticeList(2);
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
            String endDate;
            Integer test = jo.getInteger("endDate");
            if(test == null) {
                endDate = jo.getString("endDate");
            } else if(test == 0) {
                endDate = NoticeConverter.FOREVER;
            } else {
                throw new NullPointerException("End date of notice not specified.");
            }
            NoticeVO email = new NoticeVO();
            email.setTitle(title);
            email.setContent(content);
            email.setType(type);
            email.setEndDate(endDate);
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

    @RequestMapping(value = "/{id}/read", method = RequestMethod.POST)
    public void addReadRecord(@PathVariable("id") int noticeId) {
        try{
            Session session = SecurityUtils.getSubject().getSession();
            String role = String.valueOf(session.getAttribute("role"));
            if("requester".equals(role)) {
                int id = (Integer) session.getAttribute("id");
                service.addRequesterNoticeRead(id, noticeId);
            }
            if("worker".equals(role)) {
                int id = (Integer) session.getAttribute("id");
                service.addWorkerNoticeRead(id, noticeId);
            }
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("抱歉，由于未知原因，无法添加阅读记录。");
        }
    }

}

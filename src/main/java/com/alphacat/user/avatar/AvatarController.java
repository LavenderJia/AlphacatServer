package com.alphacat.user.avatar;

import com.alphacat.service.AvatarService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/avatar")
public class AvatarController {

    private AvatarService service;

    @Autowired
    public AvatarController(AvatarService service) {
        this.service = service;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public void upload(@RequestParam("file")MultipartFile file, @RequestParam("name")String name, @RequestParam("type")String type) {
        try{
            int typeInt;
            if("requester".equals(type)) {
                typeInt = 0;
            } else if("worker".equals(type)) {
                typeInt = 1;
            } else if("admin".equals(type)) {
                typeInt = 2;
            } else {
                throw new IllegalArgumentException("Cannot resolve type: " + type);
            }
            service.upload(file, name, typeInt);
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("抱歉，由于未知原因，无法上传头像。");
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Object get(@PathVariable("id") int id) {
        try{
            Session session = SecurityUtils.getSubject().getSession();
            if(id != (Integer) session.getAttribute("id")) {
                throw new IllegalArgumentException("User id not valid: " + id);
            }
            String name = String.valueOf(session.getAttribute("name"));
            String role = String.valueOf(session.getAttribute("role"));
            int type;
            if("requester".equals(role)) type = 0;
            else if("worker".equals(role)) type = 1;
            else if("requesterAdmin".equals(role) || "workerAdmin".equals(role)
                    || "superAdmin".equals(role)) type = 2;
            else throw new IllegalArgumentException("Cannot resolve user type: " + role);
            return service.get(name, type);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("抱歉，由于未知原因，无法获取头像。");
        }
    }

}

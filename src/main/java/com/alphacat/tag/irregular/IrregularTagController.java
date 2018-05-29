package com.alphacat.tag.irregular;

import com.alphacat.service.IrregularTagService;
import com.alphacat.vo.IrregularTagVO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/irregular")
public class IrregularTagController {

    @Autowired
    private IrregularTagService tagService;

    @RequestMapping(value = "/{taskId}/{picIndex}", method = RequestMethod.GET)
    @ResponseBody
    public Object get(@PathVariable("taskId") int taskId,
                      @PathVariable("picIndex") int picIndex) {
        try{
            Session session = SecurityUtils.getSubject().getSession();
            if("worker".equals(session.getAttribute("role"))) {
                int workerId = (Integer) session.getAttribute("id");
                return tagService.get(workerId, taskId, picIndex);
            }
            return new ArrayList<>();
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("抱歉，由于未知原因，无法获取不规则标记。");
        }
    }

    @RequestMapping(value = "/{taskId}/{picIndex}", method = RequestMethod.POST)
    public void save(@RequestBody IrregularTagVO tag,
                     @PathVariable("taskId") int taskId,
                     @PathVariable("picIndex") int picIndex) {
        try{
            Session session = SecurityUtils.getSubject().getSession();
            if("worker".equals(session.getAttribute("role"))) {
                int workerId = (Integer) session.getAttribute("id");
                tagService.save(tag, workerId, taskId, picIndex);
            }
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("抱歉，由于未知原因，无法存储标记。");
        }
    }

    @RequestMapping(value = "/{taskId}/{picIndex}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("taskId") int taskId,
                       @PathVariable("picIndex") int picIndex) {
        try{
            Session session = SecurityUtils.getSubject().getSession();
            if("worker".equals(session.getAttribute("role"))) {
                int workerId = (Integer) session.getAttribute("id");
                tagService.delete(workerId, taskId, picIndex);
            }
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("抱歉，由于未知原因，无法删除标记。");
        }
    }

}

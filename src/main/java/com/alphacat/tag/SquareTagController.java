package com.alphacat.tag;

import com.alphacat.service.SquareService;
import com.alphacat.vo.SquareVO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/squareTag")
public class SquareTagController {

    @Autowired
    private SquareService squareService;

    @RequestMapping(value = "/{taskId}/{picIndex}", method = RequestMethod.GET)
    @ResponseBody
    public Object getSquares(@PathVariable("taskId") int taskId,
                             @PathVariable("picIndex") int picIndex) {
        try{
            Session session = SecurityUtils.getSubject().getSession();
            if("worker".equals(session.getAttribute("role"))) {
                int workerId = (Integer) session.getAttribute("id");
                return squareService.getSquares(workerId, taskId, picIndex);
            }
            return new ArrayList<SquareVO>();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("抱歉，由于未知原因，无法获取矩形框标记。");
        }
    }

    @RequestMapping(value="/{taskId}/{picIndex}", method = RequestMethod.POST)
    public void storeSquares(@PathVariable("taskId") int taskId,
                               @PathVariable("picIndex") int picIndex,
                               @RequestBody List<SquareVO> squares) {
        try{
            Session session = SecurityUtils.getSubject().getSession();
            if("worker".equals(session.getAttribute("role"))) {
                int workerId = (Integer) session.getAttribute("id");
                squareService.saveSquares(squares, workerId, taskId, picIndex);
            }
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("抱歉，由于未知原因，无法保存标记。");
        }
    }

    @RequestMapping(value = "/{taskId}/{picIndex}", method = RequestMethod.DELETE)
    public void deleteSquares(@PathVariable("taskId") int taskId,
                                @PathVariable("picIndex") int picIndex) {
        try{
            Session session = SecurityUtils.getSubject().getSession();
            if("worker".equals(session.getAttribute("role"))) {
                int workerId = (Integer) session.getAttribute("id");
                squareService.deleteSquares(workerId, taskId, picIndex);
            }
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("抱歉，由于未知原因，无法删除标记。");
        }
    }

}

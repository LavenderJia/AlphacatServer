package com.alphacat.restController;

import com.alphacat.service.SecurityService;
import com.alphacat.service.WorkerService;
import com.alphacat.vo.WorkerVO;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/worker")
public class WorkerController {

    @Autowired
    private WorkerService workerService;
    @Autowired
    private SecurityService securityService;

    @RequestMapping("/getNormal")
    public List<WorkerVO> getNormalWorkers() {
        return workerService.getNormalWorkers();
    }

    @RequestMapping("/getBanned")
    public List<WorkerVO> getBannedWorkers() {
        return workerService.getBannedWorkers();
    }

    /**
     * 获取工人排行，按照积分大小，最多取前十名
     * @return 经过排序的结果
     */
    @RequestMapping("/getSorted")
    public List<WorkerVO> getSortedWorkers() {
        return workerService.getSortedWorkers();
    }

    @RequestMapping("/getWorker")
    public WorkerVO getWorker(@ModelAttribute("name") String name) {
        return workerService.getWorkerByName(name);
    }

    @RequestMapping("/add")
    public void addWorker(@RequestBody WorkerVO worker) {
        workerService.addWorker(worker);
    }

    @RequestMapping("/update")
    public void updateWorker(@RequestBody WorkerVO worker) {
        workerService.updateWorker(worker);
    }

    @RequestMapping("/lock")
    public void lockWorker(@ModelAttribute("id") int id) {
        workerService.setWorkerState(id, true);
    }

    @RequestMapping("/unlock")
    public void unlockWorker(@ModelAttribute("id") int id) {
        workerService.setWorkerState(id, false);
    }

    /**
     * 以name为昵称的工人有权限修改自己的密码
     * @param name 昵称
     * @param password 设定的密码
     */
    @RequestMapping("/{name}/setPassword")
    public void setPassword(@PathVariable("name") String name, @ModelAttribute("password") String password) {
        securityService.setWorkerPassword(name, password);
    }

    /**
     * 检查是否有同名的昵称
     * @param name 昵称
     * @return true=有
     */
    @RequestMapping("/checkName")
    public boolean checkName(@ModelAttribute("name")String name) {
        return workerService.hasSameName(name);
    }

    @RequestMapping("/sign")
    public void signIn() {
        int id = (int) SecurityUtils.getSubject().getSession().getAttribute("id");
        workerService.signIn(id);
    }

    @RequestMapping("/signDays")
    public int getSignDays() {
        int id = (int) SecurityUtils.getSubject().getSession().getAttribute("id");
        return workerService.getSignDays(id);
    }

    @RequestMapping("/hasSign")
    public boolean hasSignIn() {
        int id = (int) SecurityUtils.getSubject().getSession().getAttribute("id");
        return workerService.hasSigned(id);
    }
}

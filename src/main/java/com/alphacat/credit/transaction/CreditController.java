package com.alphacat.credit.transaction;

import com.alphacat.service.CreditService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/record")
public class CreditController {

    private CreditService service;

    @Autowired
    public CreditController(CreditService service) {
        this.service = service;
    }

    @RequestMapping(value = "/task", method = RequestMethod.GET)
    public Object getTransactions() {
        try{
            Session session = SecurityUtils.getSubject().getSession();
            String role = String.valueOf(session.getAttribute("role"));
            if("requester".equals(role)) {
                int id = (Integer) session.getAttribute("id");
                return service.getR_CreditTransactions(id);
            }
            if("worker".equals(role)) {
                int id = (Integer) session.getAttribute("id");
                return service.getW_CreditTransactions(id);
            }
            throw new IllegalAccessException("Cannot access credit transaction data.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("抱歉，由于未知原因，无法获取积分结算情况。");
        }
    }

}

package com.alphacat.shiro;

import com.alphacat.pojo.Admin;
import com.alphacat.pojo.Requester;
import com.alphacat.pojo.Worker;
import com.alphacat.service.SecurityService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

public class MyShiroRealm extends AuthorizingRealm {

    @Autowired
    private SecurityService securityService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Set<String> realmNames = principalCollection.getRealmNames();
        char userType = realmNames.iterator().next().charAt(0);
        Set<String> roleSet = new HashSet<>();
        switch (userType) {
            case 'r':
                roleSet.add("Requester");
                break;
            case 'w':
                roleSet.add("Worker");
                break;
            case 'a':
                roleSet.add("Admin");
                break;
        }
        Set<String> permissionSet = new HashSet<>();
        permissionSet.add(realmNames.iterator().next());
        SimpleAuthorizationInfo info =  new SimpleAuthorizationInfo();
        info.addRoles(roleSet);
        info.addStringPermissions(permissionSet);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String username = token.getUsername();
        String password = new String(token.getPassword());
        switch (username.charAt(0)) {
            case 'r' :
                Requester requester = securityService.requesterLogin(username.substring(1), password);
                subject.getSession().setAttribute("id", requester.getId());
				subject.getSession().setAttribute("name", requester.getName());
				subject.getSession().setAttribute("role", "requester");
                return new SimpleAuthenticationInfo(requester, password, username);
            case 'w' :
                Worker worker = securityService.workerLogin(username.substring(1), password);
                subject.getSession().setAttribute("id", worker.getId());
				subject.getSession().setAttribute("name", worker.getName());
				subject.getSession().setAttribute("role", "worker");
                return new SimpleAuthenticationInfo(worker, password, username);
            case 'a' :
                Admin admin = securityService.adminLogin(username.substring(1), password);
                subject.getSession().setAttribute("id", admin.getId());
				subject.getSession().setAttribute("name", admin.getName());
				subject.getSession().setAttribute("role", "admin");
                return new SimpleAuthenticationInfo(admin, password, username);
        }
        return null;
    }
}

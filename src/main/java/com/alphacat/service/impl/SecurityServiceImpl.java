package com.alphacat.service.impl;

import com.alphacat.mapper.RequesterMapper;
import com.alphacat.pojo.Requester;
import com.alphacat.mapper.WorkerMapper;
import com.alphacat.pojo.Worker;
import com.alphacat.service.SecurityService;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService {

    @Autowired
    private RequesterMapper requesterMapper;
    @Autowired
    private WorkerMapper workerMapper;

	public SecurityServiceImpl() {
		System.out.println("\n\n" + requesterMapper + "\n\n");
		System.out.println("\n\n" + workerMapper+ "\n\n");
	}

    @Override
    public Requester requesterLogin(String name, String password) throws AuthenticationException {
		System.out.println("\n\n" + requesterMapper + "\n\n");
        Requester requester = requesterMapper.getByName(name);
        if (requester == null) throw new UnknownAccountException("No such requester");
        if (requester.getState() == 1) throw new DisabledAccountException("wait to check");
        if (! requesterMapper.checkPwd(name, password)) throw new AccountException("wrong password");
        return requester;
    }

    @Override
    public Worker workerLogin(String name, String password) throws AuthenticationException {
        Worker worker = workerMapper.getByName(name);
        if (worker == null) throw new UnknownAccountException("No such worker");
        if (worker.getState() == 1) throw new DisabledAccountException("wait to check");
        if (! workerMapper.checkPwd(name, password)) throw new AccountException("wrong password");
        return worker;
    }

    @Override
    public String adminLogin(String password) throws AuthenticationException {
        return null;
    }

    @Override
    public void setRequesterPassword(String name, String password) {
        requesterMapper.setPwd(name, password);
    }

    @Override
    public void setWorkerPassword(String name, String password) {
        workerMapper.setPwd(name, password);
    }
}

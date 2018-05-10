package com.alphacat.service;

import com.alphacat.pojo.Requester;
import com.alphacat.pojo.Worker;
import org.apache.shiro.authc.AuthenticationException;

/**
 * 用于安全验证的服务
 * @author 161250102
 */
public interface SecurityService {
    /**
     * 发起者登录
     * @param name 用户名
     * @param password 明文密码
     * @return 正确登录的对象
     */
    Requester requesterLogin(String name, String password) throws AuthenticationException;
    /**
     * 工人登录
     * @param name 用户名
     * @param password 明文密码
     * @return 正确登录的对象
     */
    Worker workerLogin(String name, String password) throws AuthenticationException;
    /**
     * 工人登录
     * @param password 明文密码
     * @return 暂时随便返回一个字符串
     */
    String adminLogin(String password) throws AuthenticationException;
    /**
     * 设置发起者的密码
     * @param name 用户名
     * @param password 明文密码
     */
    void setRequesterPassword(String name, String password);
    /**
     * 设置工人的密码
     * @param name 用户名
     * @param password 明文密码
     */
    void setWorkerPassword(String name, String password);
}

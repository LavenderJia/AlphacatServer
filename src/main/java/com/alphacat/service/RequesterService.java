package com.alphacat.service;

import com.alphacat.vo.RequesterVO;

import java.util.List;

/**
 * 发起者服务接口
 * @author 161250102
 */
public interface RequesterService {

	/**
	 * @param state -1 for all, 0 for active, 1 for inactive
	 */
    List<RequesterVO> getByState(int state);

    /**
     * 发起者获取自己的个人信息
     * @param name session中保存的name
     * @return 一个发起者对象
     */
    RequesterVO getByName(String name);

	/**
	 * Get a requester vo by its id.
	 */
	RequesterVO get(int id);

    /**
     * 审核一位发起者的注册请求
     * @param id 发起者的id
     * @param isChecked 是否通过：true=通过
     */
    void checkRequester(int id, boolean isChecked);

    /**
     * It will generate a new id for this requester.
     * @param requesterVO 新增的发起者对象
     */
    void addRequester(RequesterVO requesterVO);

    /**
     * 发起者修改自己的个人信息
     * @param requesterVO 修改后的发起者对象
     */
    void updateRequester(RequesterVO requesterVO);

    /**
     * 判断是否有同名的发起者用户
     * @param name 需要检查的名称
     * @return true=有
     */
    boolean hasSameName(String name);
}

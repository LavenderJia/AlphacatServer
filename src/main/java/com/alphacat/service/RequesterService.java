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
     * Pass or not pass a requester.
     * If to pass a requester, change its state to 0;
     * If not to pass a requester, delete this requester.
     * @param id requester's id
     * @param isChecked true: pass, false: delete
     */
    void checkRequester(int id, boolean isChecked);

    /**
     * This #requesterVO does not have to have an id field or a state field.
     * It will generate a new id for this requester.
     * On default state will be set to 1.
     * @param requesterVO 新增的发起者对象
     */
    void addRequester(RequesterVO requesterVO);

    /**
     * A requester can NOT change id, password or state here.
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

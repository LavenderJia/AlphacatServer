package com.alphacat.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 获取图片信息的服务
 * @author 161250102
 */
public interface PictureService {

    /**
     * Get the picture order of a task for a worker.
     * If there is no order in database, then generate a new one.
     * Every number is in the form of %02d.
     */
    int[] getPicOrder(int taskId, int workerId);

    /**
     * 获取任务的首张图片
     * 如果是个新参与任务则自动生成图片队列并返回第一张的序号
     * 如果任务已经全部完成则返回第一张序号
     * @param taskId 任务Id
     * @param workerId 工人Id
     * @return 第一张图片的序号
     */
    int getFirstPic(int taskId, int workerId);

    /**
     *
     * @param taskId 任务Id
     * @param workerId 工人Id
     * @param picIndex 当前图片序号
     * @return 上一张图片的序号，如果已经是第一张返回负数
     */
    int getPrePic(int taskId, int workerId, int picIndex);

    /**
     *
     * @param taskId 任务Id
     * @param workerId 工人Id
     * @param picIndex 当前图片序号
     * @return 下一张图片的序号，如果已经是最后一张返回负数
     */
    int getNextPic(int taskId, int workerId, int picIndex);

    /**
     * 保存上传的文件对象
     * @param file 文件
     * @param taskId 任务Id
     * @param picIndex 图片序号
     * @return 是否上传成功
     */
    boolean uploadPic(MultipartFile file, int taskId, int picIndex);

    /**
     * 删除一个任务所有的图片
     * @param taskId 任务Id
     * @return 是否删除成功
     */
    boolean delete(int taskId);
}

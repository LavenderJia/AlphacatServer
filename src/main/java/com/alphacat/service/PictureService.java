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

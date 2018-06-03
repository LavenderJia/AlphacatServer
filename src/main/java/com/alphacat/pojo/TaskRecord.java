package com.alphacat.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 保存工人参与的任务信息，包括随机生成的图片顺序
 * @author 161250102
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class TaskRecord {

    private int workerId;
    private int taskId;
    /**
     * It should always be like the following form: "0102031112..."
     * which means the order: 1, 2, 3, 11, 12, ...
     */
    private String picOrder;
    /**
     * Records the number of pictures that the worker has tagged.
     */
    private int picDoneNum;

}

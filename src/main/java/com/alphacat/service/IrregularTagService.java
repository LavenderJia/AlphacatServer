package com.alphacat.service;

import com.alphacat.vo.IrregularTagVO;

public interface IrregularTagService {

    void save(IrregularTagVO tag, int workerId, int taskId, int picIndex);

    void delete(int workerId, int taskId, int picIndex);

    IrregularTagVO get(int workerId, int taskId, int picIndex);

}

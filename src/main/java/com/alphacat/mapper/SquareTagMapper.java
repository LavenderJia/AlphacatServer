package com.alphacat.mapper;

import com.alphacat.pojo.SquareTag;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SquareTagMapper {

    /** 获取某位工人标注在图片上的框*/
    @Select("SELECT * FROM squaretag WHERE workerId=#{workerId} AND taskId=#{taskId} AND picIndex=#{picIndex}")
    List<SquareTag> getTagsByWorker(@Param("workerId") int workerId, @Param("taskId") int taskId, @Param("picIndex") int picIndex);

    /** 获取图片上所有人的标注结果*/
    @Select("SELECT * FROM squaretag WHERE taskId=#{taskId} AND picIndex=#{picIndex}")
    SquareTag[] getAllTags(@Param("taskId") int taskId, @Param("picIndex") int picIndex);

    /** 增加一个标注*/
    @Insert("INSERT INTO squaretag(workerId, taskId, picIndex, squareIndex, x, y, h, w, labelData, description) " +
            "VALUES (#{workerId},#{taskId},#{picIndex},#{squareIndex},#{x},#{y},#{h},#{w},#{labelData},#{description})")
    void addTags(SquareTag tag);

    /** 更新已有标注*/
    @Update("UPDATE  squaretag SET x=#{x}, y=#{y}, h=#{h}, w=#{w}, labelData=#{labelData}, description=#{description} " +
            "WHERE workerId=#{workerId} AND taskId=#{taskId} AND picIndex=#{picIndex} AND squareIndex=#{squareIndex}")
    void updateTags(SquareTag tag);

    /** 判断某位工人是否标注了某张图片 */
    @Select("SELECT COUNT(*) FROM squaretag WHERE workerId=#{workerId} AND taskId=#{taskId} AND picIndex=#{picIndex}")
    boolean isExistTag(@Param("workerId") int workerId, @Param("taskId") int taskId, @Param("picIndex") int picIndex);

    /** 删除一张图片上工人的所有标注*/
    @Delete("DELETE FROM squaretag WHERE workerId=#{workerId} AND taskId=#{taskId} AND picIndex=#{picIndex}")
    void deleteTagsByWorker(@Param("workerId") int workerId, @Param("taskId") int taskId, @Param("picIndex") int picIndex);
}

package com.alphacat.mapper;

import com.alphacat.pojo.SquareTag;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SquareTagMapper {

    @Select("SELECT * FROM squaretag WHERE workerId=#{workerId} " +
                "AND taskId=#{taskId} AND picIndex=#{picIndex}")
    List<SquareTag> get(@Param("workerId") int workerId, @Param("taskId") int taskId,
                        @Param("picIndex") int picIndex);

    @Select("SELECT * FROM squaretag WHERE taskId=#{taskId} AND picIndex=#{picIndex}")
    SquareTag[] getAll(@Param("taskId") int taskId, @Param("picIndex") int picIndex);

    @Insert("INSERT INTO squaretag(workerId, taskId, picIndex, squareIndex, " +
                "x, y, h, w, labelData, description) " +
            "VALUES (#{workerId},#{taskId},#{picIndex},#{squareIndex}," +
                "#{x},#{y},#{h},#{w},#{labelData},#{description})")
    void add(SquareTag tag);

    /**
     * @deprecated To update a tag, first delete all relative tags, then add the new one.
     */
    @Update("UPDATE  squaretag SET x=#{x}, y=#{y}, h=#{h}, w=#{w}, " +
                "labelData=#{labelData}, description=#{description} " +
            "WHERE workerId=#{workerId} AND taskId=#{taskId} " +
                "AND picIndex=#{picIndex} AND squareIndex=#{squareIndex}")
    void update(SquareTag tag);

    @Select("SELECT COUNT(*) FROM squaretag WHERE workerId=#{workerId} " +
                "AND taskId=#{taskId} AND picIndex=#{picIndex}")
    boolean isExist(@Param("workerId") int workerId, @Param("taskId") int taskId,
                    @Param("picIndex") int picIndex);

    @Delete("DELETE FROM squaretag WHERE workerId=#{workerId} " +
                "AND taskId=#{taskId} AND picIndex=#{picIndex}")
    void delete(@Param("workerId") int workerId, @Param("taskId") int taskId,
                @Param("picIndex") int picIndex);

}

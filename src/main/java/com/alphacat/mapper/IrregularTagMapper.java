package com.alphacat.mapper;

import com.alphacat.pojo.IrregularTag;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IrregularTagMapper {

    @Select("SELECT * FROM irregulartag WHERE workerId=#{workerId} " +
                "AND taskId=#{taskId} AND picIndex=#{picIndex}")
    IrregularTag get(@Param("workerId") int workerId,
                     @Param("taskId") int taskId,
                     @Param("picIndex") int picIndex);

    @Select("SELECT * FROM irregulartag WHERE taskId=#{taskId} AND picIndex=#{picIndex}")
    List<IrregularTag> getAll(@Param("taskId") int taskId, @Param("picIndex") int picIndex);

    @Insert("INSERT INTO irregulartag(workerId, taskId, picIndex, figure, time) " +
            "VALUES(#{workerId},#{taskId},#{picIndex},#{figure}, CURDATE())")
    void add(IrregularTag tag);

    @Update("UPDATE irregulartag SET figure=#{figure}, time = CURDATE() " +
            "WHERE workerId=#{workerId} AND taskId=#{taskId} AND picIndex=#{picIndex}")
    void update(IrregularTag tag);

    @Delete("DELETE FROM irregulartag " +
            "WHERE workerId=#{workerId} AND taskId=#{taskId} AND picIndex=#{picIndex}")
    void delete(@Param("workerId") int workerId, @Param("taskId") int taskId,
                    @Param("picIndex") int picIndex);

    @Select("SELECT COUNT(*) FROM irregulartag " +
            "WHERE workerId=#{workerId} AND taskId=#{taskId} AND picIndex=#{picIndex}")
    boolean isExist(@Param("workerId") int workerId, @Param("taskId") int taskId,
                       @Param("picIndex") int picIndex);

}

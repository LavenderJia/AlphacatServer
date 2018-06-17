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
                "x, y, w, h, labelData, description, time) " +
            "VALUES (#{workerId},#{taskId},#{picIndex},#{squareIndex}," +
                "#{x},#{y},#{w},#{h},#{labelData},#{description}, CURDATE())")
    void add(SquareTag tag);

    /**
     * @deprecated To update a tag, first delete all relative tags, then add the new one.
     */
    @Update("UPDATE  squaretag SET x=#{x}, y=#{y}, w=#{w}, h=#{h}, " +
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

    @Select("SELECT workerId FROM squaretag " +
            "WHERE taskId = #{taskId} AND picIndex = #{picIndex} AND squareIndex = 0")
    List<Integer> getWorkersFromSquares(@Param("taskId") int taskId,
                                        @Param("picIndex") int picIndex);

    @Select("SELECT DISTINCT workerId FROM squaretag WHERE taskId = #{taskId}")
    List<Integer> getWorkers(@Param("taskId") int taskId);

    @Select("SELECT DISTINCT picIndex FROM squaretag WHERE taskId = #{taskId} AND workerId = #{workerId}")
    List<Integer> getPicsByTaskAndWorker(@Param("taskId") int taskId, @Param("workerId") int workerId);

    @Select("SELECT IFNULL(SUM(picNum), 0) FROM (" +
                "SELECT taskId, COUNT(picIndex) picNum FROM squaretag" +
                "WHERE workerId = #{workerId} GROUP BY taskId" +
            ") s JOIN (" +
                "SELECT taskId FROM task_record" +
                "WHERE workerId = #{workerId} AND rectAccuracy IS NOT NULL AND labelAccuracy IS NOT NULL" +
            ") r ON s.taskId = r.taskId")
    Long getHistoryPicNum(@Param("workerId") int workerId);

    @Select("SELECT * FROM squaretag WHERE taskId = #{taskId}")
    List<SquareTag> getByTask(@Param("taskId") int taskId);

}

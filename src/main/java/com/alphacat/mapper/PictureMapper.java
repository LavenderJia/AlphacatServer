package com.alphacat.mapper;

import com.alphacat.pojo.Picture;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PictureMapper {

    @Insert("INSERT INTO picture(pidx, taskId) VALUES(#{pidx}, #{taskId})")
    void add(Picture pic);

    @Delete("DELETE FROM picture WHERE taskId=#{taskId}")
    void multiDelete(@Param("taskId") int taskId);

    @Delete("DELETE FROM picture WHERE taskId=#{taskId} AND pidx=#{pidx}")
    void delete(@Param("taskId") int taskId, @Param("pidx") int pidx);

    @Select("SELECT COUNT(*) FROM picture WHERE taskId=#{taskId}")
    int count(int taskId);

    @Select("SELECT pidx FROM picture WHERE taskId = #{taskId}")
    List<Integer> get(@Param("taskId") int taskId);

}

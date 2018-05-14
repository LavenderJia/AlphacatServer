package com.alphacat.mapper;

import com.alphacat.pojo.Picture;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface PictureMapper {

    @Insert("INSERT INTO picture(`index`, taskId) VALUES(#{index}, #{taskId})")
    void add(Picture pic);

    @Delete("DELETE FROM picture WHERE taskId=#{taskId}")
    void delete(int taskId);

    @Select("SELECT COUNT(*) FROM picture WHERE taskId=#{taskId}")
    int count(int taskId);
}

package com.alphacat.mapper;

import com.alphacat.pojo.Label;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LabelMapper {

    @Select("SELECT * FROM label WHERE taskId=#{taskId}")
    List<Label> get(int taskId);

    @Insert("INSERT INTO label(taskId, label, choice) " +
            "VALUES (#{taskId}, #{label}, #{choice})")
    void add(Label lbl);

    /** 更新已有的标签（建议删除这个方法，标签的更新为统一更新，先删再插） */
    /**void updateLabel(Label label);*/

    @Delete("DELETE FROM label WHERE taskId=#{taskId}")
    void delete(int taskId);

}

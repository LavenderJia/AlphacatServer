package com.alphacat.mapper;

import com.alphacat.pojo.Label;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LabelMapper {

    List<Label> get(int taskId);

    void add(Label lbl);

    /** 标签内部选项数组为空时使用此方法添加*/
    @Insert("INSERT INTO label(taskId, label, choice) VALUES(#{taskId}, #{title}, '')")
    void addLabel(@Param("taskId")int taskId, @Param("title")String title);

    /** 更新已有的标签（建议删除这个方法，标签的更新为统一更新，先删再插） */
    /**void updateLabel(Label label);*/

    @Delete("DELETE FROM label WHERE taskId=#{taskId}")
    void delete(int taskId);

}

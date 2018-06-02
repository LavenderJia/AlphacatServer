package com.alphacat.mapper;

import com.alphacat.pojo.Avatar;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
public interface AvatarMapper {

    @Insert("INSERT INTO avatar(name, type, stream) " +
            "VALUES (#{name}, #{type}, #{blob})")
    void add(Avatar avatar);

    @Update("UPDATE avatar SET stream = #{blob} " +
            "WHERE name = #{name} AND type = #{type}")
    void update(Avatar avatar);

    @Select("SELECT name, type, stream FROM avatar WHERE name = #{name} AND type = #{type}")
    Avatar get(@Param("name") String name, @Param("type") int type);

    @Delete("DELETE FROM avatar WHERE name = #{name} AND type = #{type}")
    void delete(@Param("name") String name, @Param("type") int type);

}

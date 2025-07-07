package com.example.login.deposite.business.dc.dao.mapper;

import com.example.login.deposite.business.dc.model.DMember;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface DepositeMapper {
    @Insert("INSERT INTO Member(name, address, contact) VALUES(#{name}, #{address}, #{contact})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertMember(DMember member);

    @Select("SELECT id, name, address, contact FROM Member WHERE id = #{id}")
    DMember selectMemberById(@Param("id") Long id);

    @Select("SELECT id, name, address, contact FROM Member")
    List<DMember> selectAllMembers();

    @Update("UPDATE Member SET name = #{name}, address = #{address}, contact = #{contact} WHERE id = #{id}")
    int updateMember(DMember member);

    @Delete("DELETE FROM Member WHERE id = #{id}")
    int deleteMember(@Param("id") Long id);

    @Select("SELECT id, name, address, contact FROM Member WHERE name = #{name}")
    List<DMember> selectMembersByName(@Param("name") String name);
} 
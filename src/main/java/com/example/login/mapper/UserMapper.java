package com.example.login.mapper;

import com.example.login.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM users WHERE username = #{username} AND password = #{password}")
    User findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    @Select("SELECT * FROM users")
    List<User> findAll();

    @Select("SELECT * FROM users WHERE username = #{username}")
    User findByUsername(@Param("username") String username);

    @Insert("INSERT INTO users (username, password, address, age, job, company) VALUES (#{username}, #{password}, #{address}, #{age}, #{job}, #{company})")
    void insert(User user);

    @Update("UPDATE users SET password = #{password}, address = #{address}, age = #{age}, job = #{job}, company = #{company} WHERE username = #{username}")
    void update(User user);

    @Delete("DELETE FROM users WHERE username = #{username}")
    void delete(@Param("username") String username);
}

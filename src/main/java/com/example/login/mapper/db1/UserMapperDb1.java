package com.example.login.mapper.db1;

import com.example.login.model.User;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface UserMapperDb1 {
    @Select("SELECT * FROM users WHERE username = #{username} AND password = #{password}")
    User findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    @Select("SELECT * FROM users WHERE username = #{username} AND token = #{token}")
    User findByUsernameAndToken(@Param("username") String username, @Param("token") String token);

    @Select("SELECT * FROM users")
    List<User> findAll();

    @Select("SELECT * FROM users WHERE username = #{username}")
    User findByUsername(@Param("username") String username);

    @Insert("INSERT INTO users (username, password, token, address, age, job, company) VALUES (#{username}, #{password}, #{token}, #{address}, #{age}, #{job}, #{company})")
    void insert(User user);

    @Update("UPDATE users SET password = #{password}, token = #{token}, address = #{address}, age = #{age}, job = #{job}, company = #{company} WHERE username = #{username}")
    void update(User user);

    @Delete("DELETE FROM users WHERE username = #{username}")
    void delete(@Param("username") String username);
} 
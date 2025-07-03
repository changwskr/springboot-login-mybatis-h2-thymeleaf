package com.example.login.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

@Configuration
public class Db2SchemaInitializer {

    private final DataSource db2DataSource;

    public Db2SchemaInitializer(@Qualifier("db2DataSource") DataSource db2DataSource) {
        this.db2DataSource = db2DataSource;
    }

    @PostConstruct
    public void init() {
        try (Connection conn = db2DataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "username VARCHAR(50) PRIMARY KEY," +
                    "password VARCHAR(100) NOT NULL," +
                    "token VARCHAR(100)," +
                    "address VARCHAR(255)," +
                    "age INT," +
                    "job VARCHAR(100)," +
                    "company VARCHAR(100))");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 
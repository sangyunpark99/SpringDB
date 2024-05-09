package com.example.springdb.jdbc;

import static com.example.springdb.jdbc.ConnectionConst.PASSWORD;
import static com.example.springdb.jdbc.ConnectionConst.URL;
import static com.example.springdb.jdbc.ConnectionConst.USERNAME;

import java.sql.Connection;
import java.sql.DriverManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DBConnectionUtil {
    public static Connection getConnection() { // h2 database connection return
        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            log.info("get connection={}, class={}", connection, connection.getClass());
            return connection;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}

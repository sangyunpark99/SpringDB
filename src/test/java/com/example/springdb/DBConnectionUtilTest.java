package com.example.springdb;

import com.example.springdb.jdbc.DBConnectionUtil;
import java.sql.Connection;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class DBConnectionUtilTest {
    @Test
    void connection() {
        Connection connection = DBConnectionUtil.getConnection();
        Assertions.assertThat(connection).isNotNull();
    }
}
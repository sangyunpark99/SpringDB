package com.example.springdb;

import static org.assertj.core.api.Assertions.*;

import com.example.springdb.jdbc.domain.Member;
import com.example.springdb.jdbc.repository.MemberRepositoryV1;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MemberRepositoryV1Test {

    MemberRepositoryV1 repository;
    public static final String URL = "jdbc:h2:tcp://localhost/~/springDB";
    public static final String USERNAME = "sa";
    public static final String PASSWORD = "";

    @BeforeEach
    void beforeEach() throws Exception {
        // 기본 DriverManger - 항상 새로운 커넥션 획득 >> 성능이 느리다.
        // DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);

        // 커넥션 풀링: HikariProxyConnection -> JdbcConnection >> 재사용 한다.
        // conn0번을 꺼냈다가 반환하고, 꺼냈다가 반환하게 된다.
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);

        repository = new MemberRepositoryV1(dataSource);
    }

    @Test
    void crud() throws SQLException, InterruptedException {

        //save
        Member member = new Member("memberV0", 10000);
        repository.save(member);

        Member memberById = repository.findById(member.getMemberId());
        assertThat(memberById).isNotNull();

        repository.update(member.getMemberId(),20000);
        Member updateMember = repository.findById(member.getMemberId());
        assertThat(updateMember.getMoney()).isEqualTo(20000);

        repository.delete(member.getMemberId());
        assertThatThrownBy(() -> repository.findById(member.getMemberId()))
                .isInstanceOf(NoSuchElementException.class);

        Thread.sleep(1000);
    }
}

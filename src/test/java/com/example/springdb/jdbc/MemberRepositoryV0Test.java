package com.example.springdb.jdbc;

import com.example.springdb.jdbc.domain.Member;
import com.example.springdb.jdbc.repository.MemberRepositoryV0;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;

public class MemberRepositoryV0Test {
    MemberRepositoryV0 repository = new MemberRepositoryV0();

    @Test
    void crud() throws SQLException {
        // save
        Member member = new Member("MemberV0", 10000);
        repository.save(member);
    }
}

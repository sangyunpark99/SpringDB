package com.example.springdb.jdbc;

import static org.assertj.core.api.Assertions.*;

import com.example.springdb.jdbc.domain.Member;
import com.example.springdb.jdbc.repository.MemberRepositoryV0;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;

public class MemberRepositoryV0Test {

    MemberRepositoryV0 repository = new MemberRepositoryV0();

    @Test
    void crud() throws SQLException {
        // save
        Member member = new Member("MemberV0", 10000);
        repository.save(member);

        // findById
        Member findMember = repository.findById(member.getMemberId());
        System.out.println(findMember);
        assertThat(findMember).isEqualTo(member);

        // update: money: 10000 >> 20000
        repository.update(member.getMemberId(), 20000);
        Member updateMember = repository.findById(member.getMemberId());
        assertThat(updateMember.getMoney()).isEqualTo(20000);

        // delete
        repository.delete(member.getMemberId());
        assertThatThrownBy(() -> repository.findById(member.getMemberId()))
                .isInstanceOf(NoSuchElementException.class);
    }
}
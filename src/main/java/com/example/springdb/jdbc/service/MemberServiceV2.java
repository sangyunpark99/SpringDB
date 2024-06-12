package com.example.springdb.jdbc.service;

import com.example.springdb.jdbc.domain.Member;
import com.example.springdb.jdbc.repository.MemberRepositoryV2;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 트랜잭션 - 파라미터 연동, 풀을 고려한 통로
 */
@Slf4j
@RequiredArgsConstructor
public class MemberServiceV2 {

    private final DataSource dataSource;
    private final MemberRepositoryV2 memberRepository;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {

        Connection con = dataSource.getConnection();

        // 트랜잭션 적용 - connection을 넘겨 줌으로써 같은 커넥션을 가지게 된다.
        try {
            con.setAutoCommit(false); // 트랜잭션 시작
            bizLogic(con,fromId, toId, money);
            con.commit();
        } catch(Exception e) {
            con.rollback();
            throw new IllegalStateException("이체중 예외 발생");
        } finally {
            con.setAutoCommit(true);
            release(con);
        }
    }

    private void bizLogic(Connection con,String fromId, String toId, int money) throws SQLException {
        // 비즈니스 로직
        Member fromMember = memberRepository.findById(con, fromId);
        Member toMember = memberRepository.findById(con, toId);

        memberRepository.update(con, fromId, fromMember.getMoney() - money); // 돈 보내는 사람
        validation(toMember);
        memberRepository.update(con, toId, toMember.getMoney() + money); // 돈 받는 사람
    }

    private void release(Connection con) {
        if(con != null) {
            try {
                con.setAutoCommit(true); // 원상 복구 해주어야 한다.
                con.close();
            } catch (Exception e) {
                log.info("error", e);
            }
        }
    }

    private static void validation(Member toMember) {
        if(toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체중 예외 발생");
        }
    }
}

package com.example.springdb.jdbc.service;

import com.example.springdb.jdbc.domain.Member;
import com.example.springdb.jdbc.repository.MemberRepositoryV3;
import java.sql.Connection;
import java.sql.SQLException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * 트랜잭션 - 트랜잭션 매니저
 */
@Slf4j
@RequiredArgsConstructor
public class MemberServiceV3_1 {

    // private final DataSource dataSource;
    private final MemberRepositoryV3 memberRepository;
    private final PlatformTransactionManager transactionManager;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        // 트랜잭션 시작, 상태 정보 >> 커밋, 롤백할 때 필요
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        // 트랜잭션 적용 - connection을 넘겨 줌으로써 같은 커넥션을 가지게 된다.
        try {
            bizLogic(fromId, toId, money);
            transactionManager.commit(status); // 성공시 커밋
        } catch(Exception e) {
            transactionManager.rollback(status); // 실패시 롤백
            throw new IllegalStateException("이체중 예외 발생");
        }

        // 트랜잭션이 커밋되거나 롤백되면 release를 알아서 해준다.
    }

    private void bizLogic(String fromId, String toId, int money) throws SQLException {
        // 비즈니스 로직
        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);

        memberRepository.update(fromId, fromMember.getMoney() - money); // 돈 보내는 사람
        validation(toMember);
        memberRepository.update(toId, toMember.getMoney() + money); // 돈 받는 사람
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

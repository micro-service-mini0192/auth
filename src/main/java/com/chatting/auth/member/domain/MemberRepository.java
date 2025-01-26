package com.chatting.auth.member.domain;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
    private final EntityManager em;

    public Optional<Member> findByUsername(String username) {
        return Optional.ofNullable(em.createQuery("SELECT m FROM Member m WHERE m.username = :username", Member.class)
                .setParameter("username", username)
                .getSingleResult());
    }

    public boolean isExistUsername(String username) {
        Long count = em.createQuery("SELECT COUNT(m) FROM Member m WHERE m.username = :username", Long.class)
                .setParameter("username", username)
                .getSingleResult();
        return count > 0;
    }

    public boolean isExistNickname(String nickname) {
        Long count = em.createQuery("SELECT COUNT(m) FROM Member m WHERE m.nickname = :nickname", Long.class)
                .setParameter("nickname", nickname)
                .getSingleResult();
        return count > 0;
    }

    public void save(Member member) {
        if(member.getId() == null) {
            em.persist(member);
        }
    }
}
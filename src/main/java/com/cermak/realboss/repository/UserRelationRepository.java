package com.cermak.realboss.repository;

import com.cermak.realboss.model.Role;
import com.cermak.realboss.model.User;
import com.cermak.realboss.model.UserRelation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRelationRepository extends JpaRepository<UserRelation, Long> {
    List<UserRelation> findByRealman(User realman);

    List<UserRelation> findByUser(User user);

    void deleteByUserId(Long userId);

    long countUsersByRealman(User realman);
}

package com.cermak.realboss.service;

import com.cermak.realboss.model.User;

import java.util.List;

public interface UserRelationService {

    void createRelation(User realman, User user);

    List<User> getUsersByRealman(User realman);

    List<User> getRealmansByUser(User user);

    void deleteRelationsByUserId(Long userId);
}

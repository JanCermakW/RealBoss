package com.cermak.realboss.service;

import com.cermak.realboss.model.User;
import com.cermak.realboss.model.UserRelation;
import com.cermak.realboss.repository.UserRelationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserRelationServiceImpl implements UserRelationService{
    @Autowired
    private UserRelationRepository userRelationRepository;

    @Override
    public void createRelation(User realman, User user) {
        UserRelation userRelation = new UserRelation();
        userRelation.setRealman(realman);
        userRelation.setUser(user);
        userRelationRepository.save(userRelation);
    }

    @Override
    public List<User> getUsersByRealman(User realman) {
        List<UserRelation> relations = userRelationRepository.findByRealman(realman);
        return relations.stream().map(UserRelation::getUser).collect(Collectors.toList());
    }

    @Override
    public List<User> getRealmansByUser(User user) {
        List<UserRelation> relations = userRelationRepository.findByUser(user);
        return relations.stream().map(UserRelation::getRealman).collect(Collectors.toList());
    }

}

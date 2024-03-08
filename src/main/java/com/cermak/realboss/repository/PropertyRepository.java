package com.cermak.realboss.repository;

import com.cermak.realboss.model.Property;
import com.cermak.realboss.model.User;
import com.cermak.realboss.model.UserRelation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropertyRepository extends JpaRepository<Property, Long> {
    List<Property> findByRealman(User realman);

    List<Property> findByCustomer(User customer);
    long countByRealman(User realman);

    long countByCustomer(User customer);
}

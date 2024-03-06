package com.cermak.realboss.service;

import com.cermak.realboss.model.Property;
import com.cermak.realboss.model.User;

import java.util.List;

public interface PropertyService {
    void saveProperty(Property property);
    List<Property> getPropertiesByRealman(User realman);
    List<Property> getPropertiesByCustomer(User customer);

    Property getPropertyById(Long id);

    void deletPropertyById(Long id);

    long getPropertyCountForUser(User user);
}

package com.cermak.realboss.service;

import com.cermak.realboss.model.Property;
import com.cermak.realboss.model.User;
import com.cermak.realboss.model.UserRelation;
import com.cermak.realboss.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PropertyServiceImpl implements PropertyService{

    @Autowired
    private PropertyRepository propertyRepository;
    @Override
    public void saveProperty(Property property) {
        propertyRepository.save(property);
    }

    @Override
    public List<Property> getPropertiesByRealman(User realman) {
        return propertyRepository.findByRealman(realman);
    }

    @Override
    public List<Property> getPropertiesByCustomer(User customer) {
        return propertyRepository.findByCustomer(customer);
    }

    @Override
    public Property getPropertyById(Long id) {
        return propertyRepository.getById(id);
    }

    @Override
    public void deletPropertyById(Long id) {
        propertyRepository.deleteById(id);
    }
}

package com.cermak.realboss.service;

import com.cermak.realboss.model.Role;
import com.cermak.realboss.model.User;

import java.util.List;

public interface RoleService{

    List<Role> getRolesById(List<Long> id);

    List<Role> getAllRoles();

    boolean existsByName(String name);

    void saveRole(Role role);
}

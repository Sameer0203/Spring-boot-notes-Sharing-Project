package com.notesSpringProj.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.notesSpringProj.bean.Role;

public interface RoleRepo extends JpaRepository<Role, Integer> {

}

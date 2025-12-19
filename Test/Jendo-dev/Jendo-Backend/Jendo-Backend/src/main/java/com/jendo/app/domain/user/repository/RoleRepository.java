package com.jendo.app.domain.user.repository;

import com.jendo.app.domain.user.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
    List<Role> findByUserId(Long userId);
    
    void deleteByUserId(Long userId);
}

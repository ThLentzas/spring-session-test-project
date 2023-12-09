package com.example.session.role;

import com.example.session.entity.Role;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query("""
            SELECT r
            FROM Role r
            WHERE r.type = :type
        """)
    Optional<Role> findByType(@Param("type") RoleType type);
}

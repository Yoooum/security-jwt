package com.prprv.authorize.repo;

import com.prprv.authorize.entity.Role;
import com.prprv.authorize.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Yoooum
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}

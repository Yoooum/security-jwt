package com.prprv.authorize.repo;

import com.prprv.authorize.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Yoooum
 */
@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
}

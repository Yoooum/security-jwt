package com.prprv.authorize.repo;

import com.prprv.authorize.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Yoooum
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}

package com.astro.repository;

import com.astro.entity.UserMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMasterRepository extends JpaRepository<UserMaster, Integer> {
    UserMaster findByUserIdAndPassword(Integer userId, String password);
}

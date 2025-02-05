package com.astro.repository;

import com.astro.entity.UserMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserMasterRepository extends JpaRepository<UserMaster, Integer> {
  UserMaster findByUserIdAndPassword(Integer userId, String password);

   // Optional<UserMaster> findByCreatedBy(String createdBy);
   Optional<UserMaster> findByCreatedBy(String createdBy);
}

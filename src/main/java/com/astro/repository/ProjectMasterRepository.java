package com.astro.repository;

import com.astro.entity.ProjectMaster;
import io.netty.util.AsyncMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectMasterRepository extends JpaRepository<ProjectMaster, String> {

    Optional<ProjectMaster> findByProjectNameDescription(String projectNameDescription);
}

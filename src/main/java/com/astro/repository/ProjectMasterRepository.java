package com.astro.repository;

import com.astro.entity.ProjectMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectMasterRepository extends JpaRepository<ProjectMaster, String> {


}

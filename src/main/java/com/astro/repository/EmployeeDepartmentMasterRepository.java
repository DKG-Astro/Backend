package com.astro.repository;

import com.astro.entity.EmployeeDepartmentMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EmployeeDepartmentMasterRepository extends JpaRepository<EmployeeDepartmentMaster,String> {
}

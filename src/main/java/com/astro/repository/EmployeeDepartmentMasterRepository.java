package com.astro.repository;

import com.astro.entity.EmployeeDepartmentMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface EmployeeDepartmentMasterRepository extends JpaRepository<EmployeeDepartmentMaster,String> {
    Optional<EmployeeDepartmentMaster> findByEmployeeId(String employeeId);
}

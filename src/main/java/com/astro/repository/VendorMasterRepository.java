package com.astro.repository;

import com.astro.entity.VendorMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VendorMasterRepository extends JpaRepository<VendorMaster, String> {


}

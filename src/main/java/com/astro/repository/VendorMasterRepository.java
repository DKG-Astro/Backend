package com.astro.repository;

import com.astro.entity.VendorMaster;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface VendorMasterRepository extends JpaRepository<VendorMaster, String> {




}

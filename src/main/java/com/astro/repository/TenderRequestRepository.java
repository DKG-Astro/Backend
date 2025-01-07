package com.astro.repository;

import com.astro.entity.TenderRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TenderRequestRepository extends JpaRepository<TenderRequest, Long> {
}

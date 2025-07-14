package com.astro.repository.ProcurementModule;

import com.astro.dto.workflow.ProcurementDtos.SearchTenderIdDto;
import com.astro.entity.ProcurementModule.TenderRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TenderRequestRepository extends JpaRepository<TenderRequest, String> {
    Optional<TenderRequest> findByTenderId(String tenderId);

    @Query("SELECT MAX(t.tenderNumber) FROM TenderRequest t")
    Integer findMaxTenderNumber();

    @Query("SELECT new com.astro.dto.workflow.ProcurementDtos.SearchTenderIdDto(t.tenderId) " +
            "FROM TenderRequest t WHERE LOWER(t.tenderId) LIKE LOWER(CONCAT('%', :tenderId, '%'))")
    List<SearchTenderIdDto> findTenderIdLike(@Param("tenderId") String tenderId);


    @Query("SELECT new com.astro.dto.workflow.ProcurementDtos.SearchTenderIdDto(t.tenderId) " +
            "FROM TenderRequest t WHERE t.createdDate BETWEEN :startDate AND :endDate")
    List<SearchTenderIdDto> findTenderIdsBySubmittedDate(@Param("startDate") LocalDateTime startDate,
                                                         @Param("endDate") LocalDateTime endDate);

    //  TenderRequest getByTenderId(String tenderId);
}

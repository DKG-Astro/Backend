package com.astro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CsrDetailsRepository extends JpaRepository<Object, Integer> {
/*

    CsrDetails findByIdUserId(String userId);
    CsrDetails findByIdLoanNumber(String laonNumber);
    CsrDetails findByIdUserIdOrIdLoanNumber(String userId, String loanNumber);
    List<CsrDetails> findByIdLoanNumberIn(List<String> loanIdList);

    @Modifying
    @Query(value = "DELETE FROM CSR  where csrid =:csrid", nativeQuery = true)
    void deleteCsrData(@Param("csrid") String csrid);

    @Modifying
    @Query(value = "DELETE FROM LOANDETAILS  where loanId =:loanId", nativeQuery = true)
    void deleteLoanDetails(@Param("loanId") String loanId);

    @Modifying
    @Query(value = "DELETE FROM LOANEMIDETAILS  where loanId =:loanId", nativeQuery = true)
    void deleteLoanEmiDetails(@Param("loanId") String loanId);

    @Modifying
    @Query(value = "DELETE FROM CSRNBFC  where loanId =:loanId", nativeQuery = true)
    void deleteCsrNbfc(@Param("loanId") String loanId);

    @Modifying
    @Query(value = "DELETE FROM AADHAAROCRDATA  where acno =:acno", nativeQuery = true)
    void deleteAadhaarOcrData(@Param("acno") String acno);

    @Modifying
    @Query(value = "DELETE FROM CSRDOC  where csrid =:csrid", nativeQuery = true)
    void deleteCsrDocData(@Param("csrid") String csrid);

    @Modifying
    @Query(value = "DELETE FROM EVSCOREPARAMS  where acno =:acno", nativeQuery = true)
    void deleteEvscoreData(@Param("acno") String acno);

    @Modifying
    @Query(value = "DELETE FROM LICENSEOCRDATA  where acno =:acno", nativeQuery = true)
    void deleteLicenseOcrData(@Param("acno") String acno);

    @Modifying
    @Query(value = "DELETE FROM LOANFULLFILMENT  where loanId =:loanId", nativeQuery = true)
    void deleteLoanFullfilmentData(@Param("loanId") String loanId);

    @Modifying
    @Query(value = "DELETE FROM LOANPOSTDISBURSEMENT  where loanId =:loanId", nativeQuery = true)
    void deleteLoanPostDisData(@Param("loanId") String loanId);

    @Modifying
    @Query(value = "DELETE FROM LOANPREDISBURSEMENT  where loanId =:loanId", nativeQuery = true)
    void deleteLoanPreDisData(@Param("loanId") String loanId);

    @Modifying
    @Query(value = "DELETE FROM PANOCRDATA  where acno =:acno", nativeQuery = true)
    void deletePanOcrData(@Param("acno") String acno);

    @Modifying
    @Query(value = "DELETE FROM PAYMENTDETAILS  where loanId =:loanId", nativeQuery = true)
    void deletePaymentDetailsData(@Param("loanId") String loanId);

    @Modifying
    @Query(value = "DELETE FROM USERPSYTEST  where userId =:userId", nativeQuery = true)
    void deleteUserPsyData(@Param("userId") String userId);

    @Modifying
    @Query(value = "DELETE FROM USERTESTDETAILS  where userId =:userId", nativeQuery = true)
    void deleteUserTestData(@Param("userId") String userId);
*/

}

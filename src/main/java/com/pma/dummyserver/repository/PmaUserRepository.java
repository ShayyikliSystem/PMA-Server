package com.pma.dummyserver.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pma.dummyserver.enums.Classification;
import com.pma.dummyserver.model.PmaUser;

public interface PmaUserRepository extends JpaRepository<PmaUser, Long> {
    Optional<PmaUser> findByCardNumber(String cardNumber);

    Optional<PmaUser> findByshayyikliAccountNumber(Integer shayyikliAccountNumber);

    List<PmaUser> findByClassificationInAndLastReturnedCheckDateBefore(
            List<Classification> classifications, LocalDateTime date);

    List<PmaUser> findByClassificationAndLastReturnedCheckDateBefore(Classification classification, LocalDateTime date);

}
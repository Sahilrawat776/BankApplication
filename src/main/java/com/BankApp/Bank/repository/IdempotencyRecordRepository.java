package com.BankApp.Bank.repository;

import com.BankApp.Bank.model.IdempotencyRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IdempotencyRecordRepository extends JpaRepository<IdempotencyRecord, Long> {

    Optional<IdempotencyRecord>  findByIdempotencyKey(String idempotencyKey);
}

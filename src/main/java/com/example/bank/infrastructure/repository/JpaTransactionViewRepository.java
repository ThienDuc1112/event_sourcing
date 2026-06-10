package com.example.bank.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JpaTransactionViewRepository extends JpaRepository<TransactionViewEntity, Long> {
    List<TransactionViewEntity> findByAccountIdOrderByTimestampDesc(String accountId);
}

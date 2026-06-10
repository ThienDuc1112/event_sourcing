package com.example.bank.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaAccountViewRepository extends JpaRepository<AccountViewEntity, String> {
}

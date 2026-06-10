package com.example.bank.infrastructure.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {
        "com.example.bank.infrastructure.eventstore",
        "com.example.bank.infrastructure.repository"
})
@EntityScan(basePackages = {
        "com.example.bank.infrastructure.eventstore",
        "com.example.bank.infrastructure.repository"
})
public class JpaConfig {
}

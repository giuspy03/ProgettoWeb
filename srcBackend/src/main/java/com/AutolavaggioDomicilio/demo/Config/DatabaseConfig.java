package com.AutolavaggioDomicilio.demo.Config;


import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EntityScan("com.AutolavaggioDomicilio.demo.Entity")
@EnableJpaRepositories("com.AutolavaggioDomicilio.demo.Repository")
@EnableTransactionManagement
public class DatabaseConfig {
}
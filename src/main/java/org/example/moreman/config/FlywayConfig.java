package org.example.moreman.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class FlywayConfig {

    @Autowired
    private DataSource dataSource; // Assuming you have a DataSource bean configured

    @Bean
    public FlywayMigrationStrategy flywayMigrationStrategy() {
        return flyway -> {
            // Configure Flyway
            Flyway.configure()
                    .dataSource(dataSource)
                    .baselineOnMigrate(true) // Enable baselining on migrate
                    .baselineVersion("0") // Set your baseline version
                    .locations("classpath:db/migration") // Adjust your migrations path as needed
                    .defaultSchema("public") // Set your default schema
                    .load()
                    .migrate(); // Trigger migration
        };
    }
}
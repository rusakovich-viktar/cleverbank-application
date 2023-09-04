//package ru.clevertec.service.impl;
//
//import org.flywaydb.core.Flyway;
//import ru.clevertec.config.FlywayConfig;
//import ru.clevertec.config.ReaderConfigFile;
//import ru.clevertec.service.DatabaseMigrationService;
//
///**
// * Implementation of the {@link DatabaseMigrationService} interface that provides database migration services.
// */
//public class DatabaseMigrationServiceImpl implements DatabaseMigrationService {
//
//    public void migrateDatabase() {
//        ReaderConfigFile configReader = new ReaderConfigFile();
//        FlywayConfig flywayConfig = configReader.getFlywayConfig();
//
//        String flywayUrl = flywayConfig.getUrl();
//        String flywayUser = flywayConfig.getUser();
//        String flywayPassword = flywayConfig.getPassword();
//
//        Flyway flyway = Flyway.configure()
//                .dataSource(flywayUrl, flywayUser, flywayPassword)
//                .load();
//
//
//        flyway.migrate();
//    }
//}

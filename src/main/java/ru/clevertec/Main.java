package ru.clevertec;

import ru.clevertec.service.BankApplicationMainService;
import ru.clevertec.service.impl.BankApplicationMainServiceImpl;

public class Main {

    public static void main(String[] args) {
        BankApplicationMainService bankApplicationMainService = new BankApplicationMainServiceImpl();
//        DatabaseMigrationService migrationService = new DatabaseMigrationServiceImpl();
//        migrationService.migrateDatabase();
        bankApplicationMainService.doCleverBankApplicationRun();
    }
}

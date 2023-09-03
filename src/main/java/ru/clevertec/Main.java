package ru.clevertec;

import ru.clevertec.service.BankApplicationMainService;
import ru.clevertec.service.DatabaseMigrationService;
import ru.clevertec.service.impl.BankApplicationMainServiceImpl;
import ru.clevertec.service.impl.DatabaseMigrationServiceImpl;

public class Main {

    public static void main(String[] args) {
        BankApplicationMainService bankApplicationMainService = new BankApplicationMainServiceImpl();
        bankApplicationMainService.doCleverBankApplicationRun();
    }
}

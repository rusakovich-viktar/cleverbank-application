package ru.clevertec;

import ru.clevertec.service.BankApplicationMainService;
import ru.clevertec.service.impl.BankApplicationMainServiceImpl;

public class Main {

    public static void main(String[] args) {
        System.setProperty("log4j.configurationFile", "log4j2.yml");
        BankApplicationMainService bankApplicationMainService = new BankApplicationMainServiceImpl();

        bankApplicationMainService.doCleverBankApplicationRun();

    }
}

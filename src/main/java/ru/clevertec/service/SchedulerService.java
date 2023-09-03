package ru.clevertec.service;

public interface SchedulerService {
    void startPeriodicallyCalculateInterest();

    void stopPeriodicallyInterestCalculation();

}

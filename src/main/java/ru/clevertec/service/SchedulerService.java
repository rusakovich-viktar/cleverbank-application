package ru.clevertec.service;

public interface SchedulerService {
    /**
     * Starts a periodic calculation of interest.
     */
    void startPeriodicallyCalculateInterest();

    /**
     * Stops the periodic interest calculation.
     */
    void stopPeriodicallyInterestCalculation();

}

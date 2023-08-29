package ru.clevertec.service.impl;

import ru.clevertec.model.ApplicationStatus;
import ru.clevertec.service.ApplicationStatusService;

public class ApplicationStatusServiceImpl implements ApplicationStatusService {
    private final ApplicationStatus applicationStatus;

    public ApplicationStatusServiceImpl(ApplicationStatus applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    @Override
    public boolean isWorking() {
        return applicationStatus.isWorking();
    }
}

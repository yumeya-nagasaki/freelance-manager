package com.example.freelancemanager.project.validation;

import java.time.LocalDate;

public interface ProjectDateRangeRequest {

    LocalDate getStartDate();

    LocalDate getEndDate();
}
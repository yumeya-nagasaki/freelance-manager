package com.example.freelancemanager.project.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class ProjectDateRangeValidator
        implements ConstraintValidator<ValidProjectDateRange, ProjectDateRangeRequest> {

    @Override
    public boolean isValid(ProjectDateRangeRequest request, ConstraintValidatorContext context) {
        if (request == null) {
            return true;
        }

        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getEndDate();

        if (startDate == null || endDate == null) {
            return true;
        }

        return !endDate.isBefore(startDate);
    }
}
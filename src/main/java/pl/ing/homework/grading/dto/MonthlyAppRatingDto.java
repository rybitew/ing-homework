package pl.ing.homework.grading.dto;

import java.util.UUID;

public interface MonthlyAppRatingDto {
    String getAppName();
    UUID getAppId();
    Double getRatingThisMonth();
    Double getRatingPreviousMonth();
}

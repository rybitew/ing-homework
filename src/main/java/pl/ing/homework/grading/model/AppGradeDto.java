package pl.ing.homework.grading.model;

import java.util.UUID;

public record AppGradeDto(UUID appUuid, String app_name, Double rating, Integer reviewerAge, String reviewerCountry) {
}

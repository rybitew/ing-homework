package pl.ing.homework.grading.model;

import java.util.UUID;

public class MonthlyAppRatingImpl implements MonthlyAppRatingDto {
    private String appName;
    private UUID appId;
    private Double ratingThisMonth;
    private Double ratingPreviousMonth;

    public MonthlyAppRatingImpl appName(String appName) {
        this.appName = appName;
        return this;
    }

    public MonthlyAppRatingImpl appId(String uuid) {
        this.appId = UUID.fromString(uuid);
        return this;
    }

    public MonthlyAppRatingImpl ratingThisMonth(Double rating) {
        this.ratingThisMonth = rating;
        return this;
    }

    public MonthlyAppRatingImpl ratingPreviousMonth(Double rating) {
        this.ratingPreviousMonth = rating;
        return this;
    }

    public String getAppName() {
        return appName;
    }

    public UUID getAppId() {
        return appId;
    }

    public Double getRatingThisMonth() {
        return ratingThisMonth;
    }

    public Double getRatingPreviousMonth() {
        return ratingPreviousMonth;
    }
}

package pl.ing.homework.grading.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(
        name = "APP_GRADE",
        indexes = {
                @Index(name = "appId_ix", columnList = "appId"),
                @Index(name = "createDate_ix", columnList = "createDate")
        }
)
public class AppGradeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID appId;
    private String name;
    private Double rating;
    private Integer reviewerAge;
    private String reviewerCountry;

    private LocalDate createDate;

    public AppGradeEntity() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getAppId() {
        return appId;
    }

    public void setAppId(UUID appId) {
        this.appId = appId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getReviewerAge() {
        return reviewerAge;
    }

    public void setReviewerAge(Integer reviewerAge) {
        this.reviewerAge = reviewerAge;
    }

    public String getReviewerCountry() {
        return reviewerCountry;
    }

    public void setReviewerCountry(String reviewerCountry) {
        this.reviewerCountry = reviewerCountry;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }
}

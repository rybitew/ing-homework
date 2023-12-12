package pl.ing.homework.grading.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.util.UUID;

@Entity
public class AppGradeEntity {
    @Id
    private UUID id;
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

package pl.ing.homework.grading.dto;

public class AppAverageDto {
    private Double average;

    public AppAverageDto(Double average) {
        this.average = average;
    }

    public Double getAverage() {
        return average;
    }

    public void setAverage(Double average) {
        this.average = average;
    }
}

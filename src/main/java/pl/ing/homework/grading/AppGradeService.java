package pl.ing.homework.grading;

import org.springframework.stereotype.Service;
import pl.ing.homework.grading.model.AgeGroup;
import pl.ing.homework.grading.model.TopAppGradeDto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class AppGradeService {
    private final AppGradeRepository appGradeRepository;

    public AppGradeService(AppGradeRepository appGradeRepository) {
        this.appGradeRepository = appGradeRepository;
    }

    public Double getAverage(UUID appId, LocalDate since, LocalDate until) {
        return appGradeRepository.findAverageBetweenDates(appId, since, until);
    }

    public List<TopAppGradeDto> getTop100InAgeGroup(AgeGroup ageGroup, LocalDate since, LocalDate until) {
        if (!AgeGroup.AGE_GROUP_7.equals(ageGroup))
            return appGradeRepository.findByAgeGroupBetweenDates(ageGroup.floor(), ageGroup.ceiling(), since, until);
        return appGradeRepository.findByOlderThanAgeBetweenDates(ageGroup.floor(), since, until);
    }
}

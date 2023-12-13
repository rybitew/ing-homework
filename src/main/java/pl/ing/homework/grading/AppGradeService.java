package pl.ing.homework.grading;

import org.springframework.stereotype.Service;
import pl.ing.homework.grading.dto.TopAppGradeDto;
import pl.ing.homework.grading.model.AgeGroup;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
class AppGradeService {
    private final AppGradeRepository repository;

    AppGradeService(AppGradeRepository repository) {
        this.repository = repository;
    }

    Double getAverage(UUID appId, LocalDate since, LocalDate until) {
        return repository.findAverageBetweenDates(appId, since, until);
    }

    List<TopAppGradeDto> getTop100InAgeGroup(AgeGroup ageGroup, LocalDate since, LocalDate until) {
        if (!AgeGroup.AGE_GROUP_7.equals(ageGroup))
            return repository.findByAgeGroupBetweenDates(ageGroup.floor(), ageGroup.ceiling(), since, until);
        return repository.findByOlderThanAgeBetweenDates(ageGroup.floor(), since, until);
    }
}

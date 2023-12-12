package pl.ing.homework.grading;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.ing.homework.grading.model.AgeGroup;
import pl.ing.homework.grading.model.TopAppGradeDto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class AppGradeController {
    private final AppGradeService appGradeService;

    public AppGradeController(AppGradeService appGradeService) {
        this.appGradeService = appGradeService;
    }

    @GetMapping("/{appUuid}/avg")
    public ResponseEntity<Double> getAverageBetweenDates(
            @PathVariable("appUuid") UUID appId,
            @RequestParam("since") @DateTimeFormat(pattern = "yyyyMMdd") LocalDate since,
            @RequestParam("until") @DateTimeFormat(pattern = "yyyyMMdd") LocalDate until
    ) {
        return ResponseEntity.ok(appGradeService.getAverage(appId, since, until));
    }

    @GetMapping("/top-apps/{ageGroup}")
    public ResponseEntity<List<TopAppGradeDto>> getByAgeGroupAndDate(
            @PathVariable("ageGroup") AgeGroup ageGroup,
            @RequestParam("since") @DateTimeFormat(pattern = "yyyyMMdd") LocalDate since,
            @RequestParam("until") @DateTimeFormat(pattern = "yyyyMMdd") LocalDate until
    ) {
        return ResponseEntity.ok(appGradeService.getTop100InAgeGroup(ageGroup, since, until));
    }
}

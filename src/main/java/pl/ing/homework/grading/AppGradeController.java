package pl.ing.homework.grading;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.ing.homework.grading.dto.AppAverageDto;
import pl.ing.homework.grading.dto.TopAppGradeDto;
import pl.ing.homework.grading.model.AgeGroup;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class AppGradeController {
    private final AppGradeService appGradeService;
    private final AppDataImportService appDataImportService;

    public AppGradeController(AppGradeService appGradeService, AppDataImportService appDataImportService) {
        this.appGradeService = appGradeService;
        this.appDataImportService = appDataImportService;
    }

    @GetMapping("/{appUuid}/avg")
    public ResponseEntity<AppAverageDto> getAverageBetweenDates(
            @PathVariable("appUuid") UUID appId,
            @RequestParam("since") @DateTimeFormat(pattern = "yyyyMMdd") LocalDate since,
            @RequestParam("until") @DateTimeFormat(pattern = "yyyyMMdd") LocalDate until
    ) {
        return ResponseEntity.ok(new AppAverageDto(appGradeService.getAverage(appId, since, until)));
    }

    @GetMapping("/top-apps/{ageGroup}")
    public ResponseEntity<List<TopAppGradeDto>> getByAgeGroupAndDate(
            @PathVariable("ageGroup") AgeGroup ageGroup,
            @RequestParam("since") @DateTimeFormat(pattern = "yyyyMMdd") LocalDate since,
            @RequestParam("until") @DateTimeFormat(pattern = "yyyyMMdd") LocalDate until
    ) {
        return ResponseEntity.ok(appGradeService.getTop100InAgeGroup(ageGroup, since, until));
    }

    @PostMapping("/util/force-import")
    public ResponseEntity<Void> forceImport() {
        appDataImportService.importData();
        return ResponseEntity.ok().build();
    }
}

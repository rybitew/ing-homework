package pl.ing.homework.grading;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/utils")
public class UtilsController {
    private final AppDataImportService appDataImportService;
    private final AppReportService appReportService;

    public UtilsController(AppDataImportService appDataImportService, AppReportService appReportService) {
        this.appDataImportService = appDataImportService;
        this.appReportService = appReportService;
    }

    @PostMapping("/force-import")
    public ResponseEntity<Void> forceImport() {
        appDataImportService.importData();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/force-export")
    public ResponseEntity<Void> forceExport() {
        appReportService.generateAppReports();
        return ResponseEntity.ok().build();
    }
}

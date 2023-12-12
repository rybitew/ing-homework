package pl.ing.homework.grading;

import org.springframework.stereotype.Service;

@Service
public class AppStatsExportService {
    private final AppGradeRepository appGradeRepository;

    public AppStatsExportService(AppGradeRepository appGradeRepository) {
        this.appGradeRepository = appGradeRepository;
    }


}

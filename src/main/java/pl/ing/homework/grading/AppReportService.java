package pl.ing.homework.grading;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.ing.homework.grading.model.MonthlyAppRatingDto;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
class AppReportService {
    @Value("${export.dir}")
    private String outputPath;
    private static final String HEADER = "app_name,app_uuid,rating_this_month,rating_previous_month\n";
    private final AppGradeRepository repository;

    public AppReportService(AppGradeRepository repository) {
        this.repository = repository;
    }

    @Scheduled(cron = "0 5 20 L * *")
    public void generateAppReports() {
        YearMonth date = YearMonth.now();
        List<MonthlyAppRatingDto> topApps = repository.findTopTrendingByMonth(date.getYear(), date.getMonthValue());
        List<MonthlyAppRatingDto> decliningApps = repository.findDecliningAppsByMonth(date.getYear(), date.getMonthValue());

        generateCsv(topApps, date, "trending100apps");
        generateCsv(decliningApps, date, "apps-with-issues");
    }

    private void generateCsv(List<MonthlyAppRatingDto> apps, YearMonth date, String filePrefix) {
        File outputDir = new File(outputPath);
        if (!outputDir.isDirectory() && !outputDir.mkdirs()) {
            throw new RuntimeException("Nie udało się utworzyć folderu wynikowego: " + outputPath);
        }

        String fileName = filePrefix + "-" + date.format(DateTimeFormatter.ofPattern("yyyyMM")) + ".csv";
        try (FileWriter writer = new FileWriter(outputPath + "/" + fileName)) {
            writer.write(HEADER);
            apps.forEach(app -> {
                try {
                    writer.write(app.getAppName() + "," + app.getAppId() + "," + app.getRatingThisMonth() + "," +
                            app.getRatingPreviousMonth() + "\n");
                } catch (IOException e) {
                    throw new RuntimeException("Błąd podczas tworzenia raportu " + filePrefix + " z miesiąca " + date, e);
                }
            });
            System.out.println("Wygenerowano raport " + filePrefix + " z miesiąca " + date);
        } catch (IOException e) {
            throw new RuntimeException("Błąd podczas tworzenia raportu " + filePrefix + " z miesiąca " + date, e);
        }
    }
}

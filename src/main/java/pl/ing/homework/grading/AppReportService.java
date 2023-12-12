package pl.ing.homework.grading;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.ing.homework.grading.model.MonthlyAppRating;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class AppReportService {
    private static final String OUTPUT_PATH = "reports";
    private static final String HEADER = "app_name,app_uuid,rating_this_month,rating_previous_month\n";
    private final AppGradeRepository repository;

    public AppReportService(AppGradeRepository repository) {
        this.repository = repository;
    }

    //    @Scheduled(cron = "* 5 20 L * *")
    @Scheduled(fixedRate = 120, timeUnit = TimeUnit.MINUTES, initialDelay = 1)
    public void generateAppReports() {
//        YearMonth date = YearMonth.now();
        YearMonth date = YearMonth.of(2025, 4);
        List<MonthlyAppRating> topApps = repository.findTopTrendingByMonth(date.getYear(), date.getMonthValue());
        List<MonthlyAppRating> decliningApps = repository.findDecliningAppsByMonth(date.getYear(), date.getMonthValue());

        generateCsv(topApps, date, "trending100apps");
        generateCsv(decliningApps, date, "apps-with-issues");
    }

    private void generateCsv(List<MonthlyAppRating> apps, YearMonth date, String filePrefix) {
        File outputDir = new File(OUTPUT_PATH);
        if (!outputDir.isDirectory() && !outputDir.mkdir()) {
            throw new RuntimeException("Nie udało się utworzyć folderu wynikowego 'reports'");
        }

        String fileName = filePrefix + "-" + date.format(DateTimeFormatter.ofPattern("yyyyMM")) + ".csv";
        try (FileWriter writer = new FileWriter(OUTPUT_PATH + "/" + fileName)) {
            writer.write(HEADER);
            apps.forEach(app -> {
                try {
                    writer.write(app.getAppName() + "," + app.getAppId() + "," + app.getRatingThisMonth() + "," +
                            app.getRatingPreviousMonth() + "\n");
                } catch (IOException e) {
                    System.err.println("Błąd podczas tworzenia raportu " + filePrefix + " z miesiąca " + date);
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            });
            System.out.println("Wygenerowano raport " + filePrefix + " z miesiąca " + date);
        } catch (IOException e) {
            System.err.println("Błąd podczas tworzenia raportu " + filePrefix + " z miesiąca " + date);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}

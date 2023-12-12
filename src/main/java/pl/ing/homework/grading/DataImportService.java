package pl.ing.homework.grading;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.ing.homework.grading.model.AppGradeEntity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Service
public class DataImportService {
    private static final String FILENAME_TEMPLATE = "app_rating-{DATE}.csv";

    private static final int APP_NAME = 0;
    private static final int APP_UUID = 1;
    private static final int RATING = 2;
    private static final int REVIEWER_AGE = 3;
    private static final int REVIEWER_COUNTRY = 4;

    @Value("${import.dir}")
    private String pathToDirectory;
    private final AppGradeRepository repository;
    private static final Pattern p = Pattern.compile("^([a-zA-Z]+)([0-9]+)(.*)");


    public DataImportService(AppGradeRepository repository) {
        this.repository = repository;
    }

    //    @Scheduled(cron = "* 1 20 * * *")
    @Scheduled(fixedRate = 2, timeUnit = TimeUnit.MINUTES)
    public void importData() {
        String date = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        File[] dataFiles = new File(pathToDirectory).listFiles();
        if (dataFiles == null) {
            System.err.println("Wystąpił błąd w dostępie do folderu z danymi: " + pathToDirectory);
            return;
        }
        Set<LocalDate> processedDates = repository.findAllImportedDates();
        Arrays.stream(dataFiles)
                .filter(file -> !processedDates.contains(extractDateFromName(file)))
                .forEach(file -> {
                    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                        reader.readLine();
                        List<AppGradeEntity> entities = reader.lines().skip(1)
                                .map(line -> mapToEntity(line, extractDateFromName(file)))
                                .toList();

                        repository.saveAll(entities);
                    } catch (IOException e) {
                        System.err.println("Błąd podczas przetwarzania pliku z dnia " + date);
                    }
                });
//        File file = new File(pathToDirectory + "/" + FILENAME_TEMPLATE.replace("{DATE}", date));
//
//        if (!file.exists()) {
//            System.err.println("Brak pliku dla dnia " + date);
//            return;
//        }
//
//        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
//            reader.readLine();
//            List<AppGradeEntity> entities = reader.lines().skip(1).map(this::mapToEntity).toList();
//
//            repository.saveAll(entities);
//        } catch (IOException e) {
//            System.err.println("Błąd podczas przetwarzania pliku z dnia " + date);
//        }
    }

    private LocalDate extractDateFromName(File file) {
        String[] nameParts = file.getName().split("[-.]");
        return LocalDate.of(
                Integer.parseInt(nameParts[1]), Integer.parseInt(nameParts[2]), Integer.parseInt(nameParts[3])
        );
    }

    private AppGradeEntity mapToEntity(String line, LocalDate createDate) {
        String[] cols = line.split(",");
        AppGradeEntity entity = new AppGradeEntity();
        entity.setName(cols[APP_NAME]);
        entity.setId(UUID.fromString(cols[APP_UUID]));
        entity.setRating(Double.parseDouble(cols[RATING]));
        entity.setReviewerAge(Integer.parseInt(cols[REVIEWER_AGE]));
        entity.setReviewerCountry(cols[REVIEWER_COUNTRY]);
        entity.setCreateDate(createDate);
        return entity;
    }
}

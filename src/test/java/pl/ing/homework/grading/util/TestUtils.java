package pl.ing.homework.grading.util;

import pl.ing.homework.grading.model.MonthlyAppRatingDto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestUtils {
    private TestUtils() {}

    public static void validateResult(File resultFile, List<MonthlyAppRatingDto> apps) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(resultFile))) {
            assertEquals("app_name,app_uuid,rating_this_month,rating_previous_month", reader.readLine());
            for (MonthlyAppRatingDto app : apps) {
                assertEquals(toCsvString(app), reader.readLine());
            }
        }
    }

    private static String toCsvString(MonthlyAppRatingDto rating) {
        return String.join(",", rating.getAppName(), rating.getAppId().toString(),
                rating.getRatingThisMonth().toString(), rating.getRatingPreviousMonth().toString());
    }
}

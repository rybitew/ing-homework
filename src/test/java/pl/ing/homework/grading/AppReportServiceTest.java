package pl.ing.homework.grading;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import pl.ing.homework.grading.dto.MonthlyAppRatingDto;
import pl.ing.homework.grading.model.MonthlyAppRatingImpl;

import java.io.File;
import java.io.IOException;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static pl.ing.homework.grading.util.TestUtils.validateResult;

@ExtendWith(SpringExtension.class)
@TestPropertySource("classpath:application.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AppReportServiceTest {
    @Value("${export.dir}")
    String outputPath;
    AppReportService appReportService;
    @Mock
    AppGradeRepository repository;

    @BeforeAll
    void setup() {
        appReportService = new AppReportService(repository);
        ReflectionTestUtils.setField(appReportService, "outputPath", outputPath);
    }

    @AfterAll
    void cleanUp() {
        File outputDir = new File(outputPath);
        if (outputDir.isDirectory()) {
            for (File file : outputDir.listFiles()) {
                file.delete();
            }
            outputDir.delete();
        }
    }

    @Test
    void shouldGenerateTrendingAppsAndAppsWithIssuesReports() throws IOException {

        when(repository.findTopTrendingByMonth(anyInt(), anyInt())).thenReturn(getTopTrendingApps());
        when(repository.findDecliningAppsByMonth(anyInt(), anyInt())).thenReturn(getDecliningApps());

        appReportService.generateAppReports();

        String date = YearMonth.now().format(DateTimeFormatter.ofPattern("yyyyMM"));

        File trendingAppsFile = new File(outputPath + "/trending100apps-" + date + ".csv");
        File issueAppsFile = new File(outputPath + "/apps-with-issues-" + date + ".csv");

        validateResult(trendingAppsFile, getTopTrendingApps());
        validateResult(issueAppsFile, getDecliningApps());
    }

    private List<MonthlyAppRatingDto> getTopTrendingApps() {
        MonthlyAppRatingDto rating1 = new MonthlyAppRatingImpl()
                .appId("cc5e76ae-3c6a-11ee-be56-0242ac120002")
                .appName("Quickphoto")
                .ratingThisMonth(5.0)
                .ratingPreviousMonth(3.5);
        MonthlyAppRatingDto rating2 = new MonthlyAppRatingImpl()
                .appId("e73bb57c-3c6a-11ee-be56-0242ac120002")
                .appName("Magic Scanner")
                .ratingThisMonth(4.4)
                .ratingPreviousMonth(4.0);
        return List.of(rating1, rating2);
    }

    private List<MonthlyAppRatingDto> getDecliningApps() {
        MonthlyAppRatingDto rating1 = new MonthlyAppRatingImpl()
                .appId("9ed0a148-731b-46c9-a01d-cdd29a931cd1")
                .appName("SciCalc")
                .ratingThisMonth(3.5)
                .ratingPreviousMonth(4.5);
        MonthlyAppRatingDto rating2 = new MonthlyAppRatingImpl()
                .appId("e73bb57c-3c6a-11ee-be56-0242ac120002")
                .appName("Magic Scanner")
                .ratingThisMonth(4.4)
                .ratingPreviousMonth(5.0);
        return List.of(rating1, rating2);
    }
}
/*
//'2025-03-24', 4.5, 47, '9ed0a148-731b-46c9-a01d-cdd29a931cd1', 'd3e434e7-fab2-4b63-b78e-31811f4a701a', 'SciCalc', 'US'
//'2025-03-24', 4.0, 28, '9ed0a148-731b-46c9-a01d-cdd29a931cd1', 'd432ba34-2bbd-4a6a-8621-d6f16cd1bc64', 'SciCalc', 'PL'
//'2025-03-24', 5.0, 34, '6ab3aa00-3c6a-11ee-be56-0242ac120002', '10bed4d0-7bd3-47cb-9f1f-ed0b6a9753ec', 'Mobile Notepad', 'CN'
//'2025-03-24', 3.5, 19, 'cc5e76ae-3c6a-11ee-be56-0242ac120002', '44b0cb1d-cb85-46c9-bd06-b95904c5ffb4', 'Quickphoto', 'US'
//'2025-03-24', 4.0, 20, 'e73bb57c-3c6a-11ee-be56-0242ac120002', 'c91291ed-70b0-4bcb-82f8-5aa15fa0670b', 'Magic Scanner', 'US'
//'2025-03-24', 5.0, 18, '9ed0a148-731b-46c9-a01d-cdd29a931cd1', '0cbc3c99-53eb-4a86-bd2c-8da11b2d0976', 'SciCalc', 'US'
//'2025-03-24', 4.0, 23, '6ab3aa00-3c6a-11ee-be56-0242ac120002', 'e0d38851-4c0c-4af0-a7d9-f6d546767be6', 'Mobile Notepad', 'CN'
//'2025-04-24', 4.1, 47, '9ed0a148-731b-46c9-a01d-cdd29a931cd1', '968ac209-3923-4421-9547-51981cb6205e', 'SciCalc', 'US'
//'2025-04-24', 4.5, 28, '9ed0a148-731b-46c9-a01d-cdd29a931cd1', 'd6dea810-6e8d-475a-b511-e63794972bd5', 'SciCalc', 'PL'
//'2025-04-24', 4.8, 34, '6ab3aa00-3c6a-11ee-be56-0242ac120002', '9128dc23-46d3-4937-b57e-0cc123032444', 'Mobile Notepad', 'CN'
//'2025-04-24', 5.0, 19, 'cc5e76ae-3c6a-11ee-be56-0242ac120002', 'b569248c-3ff4-4422-8644-196bdda229ae', 'Quickphoto', 'US'
//'2025-04-24', 4.4, 20, 'e73bb57c-3c6a-11ee-be56-0242ac120002', 'caca05fd-a5f4-47c5-a35e-0bff95eb746a', 'Magic Scanner', 'US'
//'2025-04-24', 2.0, 18, '9ed0a148-731b-46c9-a01d-cdd29a931cd1', '61cdc9cb-2d2d-48c0-8939-f522e81f6cc2', 'SciCalc', 'US'
//'2025-04-24', 4.1, 23, '6ab3aa00-3c6a-11ee-be56-0242ac120002', '39a8ff95-8e87-4699-ba7b-e4d74afa03fb', 'Mobile Notepad', 'CN'
        manager.createNativeQuery("""
                insert into APP_GRADE (CREATE_DATE, RATING, REVIEWER_AGE, APP_ID, ID, NAME, REVIEWER_COUNTRY) values
                ('2025-03-24', 4.5, 47, '9ed0a148-731b-46c9-a01d-cdd29a931cd1', 'd3e434e7-fab2-4b63-b78e-31811f4a701a', 'SciCalc', 'US'),
                ('2025-03-24', 4.0, 28, '9ed0a148-731b-46c9-a01d-cdd29a931cd1', 'd432ba34-2bbd-4a6a-8621-d6f16cd1bc64', 'SciCalc', 'PL'),
                ('2025-03-24', 5.0, 34, '6ab3aa00-3c6a-11ee-be56-0242ac120002', '10bed4d0-7bd3-47cb-9f1f-ed0b6a9753ec', 'Mobile Notepad', 'CN'),
                ('2025-03-24', 3.5, 19, 'cc5e76ae-3c6a-11ee-be56-0242ac120002', '44b0cb1d-cb85-46c9-bd06-b95904c5ffb4', 'Quickphoto', 'US'),
                ('2025-03-24', 4.0, 20, 'e73bb57c-3c6a-11ee-be56-0242ac120002', 'c91291ed-70b0-4bcb-82f8-5aa15fa0670b', 'Magic Scanner', 'US'),
                ('2025-03-24', 5.0, 18, '9ed0a148-731b-46c9-a01d-cdd29a931cd1', '0cbc3c99-53eb-4a86-bd2c-8da11b2d0976', 'SciCalc', 'US'),
                ('2025-03-24', 4.0, 23, '6ab3aa00-3c6a-11ee-be56-0242ac120002', 'e0d38851-4c0c-4af0-a7d9-f6d546767be6', 'Mobile Notepad', 'CN'),
                ('2025-04-24', 4.1, 47, '9ed0a148-731b-46c9-a01d-cdd29a931cd1', '968ac209-3923-4421-9547-51981cb6205e', 'SciCalc', 'US'),
                ('2025-04-24', 4.5, 28, '9ed0a148-731b-46c9-a01d-cdd29a931cd1', 'd6dea810-6e8d-475a-b511-e63794972bd5', 'SciCalc', 'PL'),
                ('2025-04-24', 4.8, 34, '6ab3aa00-3c6a-11ee-be56-0242ac120002', '9128dc23-46d3-4937-b57e-0cc123032444', 'Mobile Notepad', 'CN'),
                ('2025-04-24', 5.0, 19, 'cc5e76ae-3c6a-11ee-be56-0242ac120002', 'b569248c-3ff4-4422-8644-196bdda229ae', 'Quickphoto', 'US'),
                ('2025-04-24', 4.4, 20, 'e73bb57c-3c6a-11ee-be56-0242ac120002', 'caca05fd-a5f4-47c5-a35e-0bff95eb746a', 'Magic Scanner', 'US'),
                ('2025-04-24', 2.0, 18, '9ed0a148-731b-46c9-a01d-cdd29a931cd1', '61cdc9cb-2d2d-48c0-8939-f522e81f6cc2', 'SciCalc', 'US'),
                ('2025-04-24', 4.1, 23, '6ab3aa00-3c6a-11ee-be56-0242ac120002', '39a8ff95-8e87-4699-ba7b-e4d74afa03fb', 'Mobile Notepad', 'CN')
                """).executeUpdate();



 */
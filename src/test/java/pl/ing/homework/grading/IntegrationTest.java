package pl.ing.homework.grading;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import pl.ing.homework.grading.dto.TopAppGradeDto;
import pl.ing.homework.grading.model.AgeGroup;
import pl.ing.homework.grading.model.TopAppGradeImpl;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.ing.homework.grading.util.TestUtils.validateResult;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class IntegrationTest {

    @Value("${export.dir}")
    String outputPath;
    @LocalServerPort
    private int port;
    @Autowired
    AppGradeRepository repository;
    @Autowired
    AppReportService appReportService;

    DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyyMMdd");

    @BeforeAll
    void setup() {
        RestAssured.port = port;
        await().untilAsserted(() ->
                RestAssured.post("/utils/force-import").then().statusCode(HttpStatus.OK.value())
        );
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
    void contextLoads() {
    }

    @Test
    void shouldImportDataFiles() {
        assertEquals(14, repository.findAll().size());
    }

    @Test
    void shouldGenerateReports() throws IOException {
        appReportService.generateAppReports();
        YearMonth yearMonth = YearMonth.now();
        String date = yearMonth.format(DateTimeFormatter.ofPattern("yyyyMM"));

        File trendingAppsFile = new File(outputPath + "/trending100apps-" + date + ".csv");
        File issueAppsFile = new File(outputPath + "/apps-with-issues-" + date + ".csv");


        validateResult(trendingAppsFile, repository.findTopTrendingByMonth(yearMonth.getYear(), yearMonth.getMonthValue()));
        validateResult(issueAppsFile, repository.findDecliningAppsByMonth(yearMonth.getYear(), yearMonth.getMonthValue()));
    }

    @Test
    void shouldReturnAverageWithStatusOk() {
        String uuid = "9ed0a148-731b-46c9-a01d-cdd29a931cd1";
        Float avg = repository.findAverageBetweenDates(
                UUID.fromString(uuid),
                LocalDate.parse("20250101", pattern),
                LocalDate.parse("20250505", pattern)
        ).floatValue();

        RestAssured.when().get("/api/" + uuid + "/avg?since=20250101&until=20250505")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("average", Matchers.equalTo(avg));
    }

    @Test
    void shouldReturnNullAverageWithStatusOk() {
        RestAssured.when().get("/api/" + UUID.randomUUID() + "/avg?since=20250101&until=20250505")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("average", Matchers.equalTo(null));
    }

    @ParameterizedTest
    @EnumSource(AgeGroup.class)
    void shouldReturnTopAppsInAgeGroupWithStatusOk(AgeGroup group) {
        List<TopAppGradeImpl> topApps;
        if (!group.equals(AgeGroup.AGE_GROUP_7)) {
            topApps = mapToImpl(repository.findByAgeGroupBetweenDates(
                    group.floor(),
                    group.ceiling(),
                    LocalDate.parse("20250101", pattern),
                    LocalDate.parse("20250505", pattern)
            ));
        } else {
            topApps = mapToImpl(repository.findByOlderThanAgeBetweenDates(
                    group.floor(),
                    LocalDate.parse("20250101", pattern),
                    LocalDate.parse("20250505", pattern)
            ));
        }

        assertEquals(topApps, RestAssured.get("/api/top-apps/" + group + "?since=20250101&until=20250505")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath().getList(".", TopAppGradeImpl.class));
    }

    @Test
    void shouldReturnEmptyListOfAppsInAgeGroupWithStatusOk() {
        assertTrue(RestAssured.get("/api/top-apps/" + AgeGroup.AGE_GROUP_1 + "?since=20220101&until=20220505")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath().getList(".", TopAppGradeImpl.class).isEmpty());
    }

    private List<TopAppGradeImpl> mapToImpl(List<TopAppGradeDto> list) {
        return list.stream().map(it -> new TopAppGradeImpl().appId(it.getAppId()).name(it.getName())).toList();
    }
}

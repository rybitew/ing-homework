package pl.ing.homework.grading;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import pl.ing.homework.grading.model.AppGradeEntity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@TestPropertySource("classpath:application.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AppDataImportServiceTest {
    @Value("${import.test.dir}")
    String inputPath;
    AppDataImportService service;
    @Mock
    AppGradeRepository repository;

    @BeforeAll
    void setup() {
        service = new AppDataImportService(repository);
        ReflectionTestUtils.setField(service, "pathToDirectory", inputPath);
    }

    @Test
    void shouldImportDataFile() {
        when(repository.findAllImportedDates()).thenReturn(Collections.emptySet());

        service.importData();

        ArgumentCaptor<List<AppGradeEntity>> captor = ArgumentCaptor.forClass(List.class);

        verify(repository).saveAll(captor.capture());

        List<AppGradeEntity> importedEntities = captor.getValue();

        assertEquals(4, importedEntities.size());
        assertEquals(UUID.fromString("9ed0a148-731b-46c9-a01d-cdd29a931cd1"), importedEntities.get(0).getAppId());
        assertEquals("SciCalc", importedEntities.get(0).getName());
        assertEquals(4.5, importedEntities.get(0).getRating());
        assertEquals(47, importedEntities.get(0).getReviewerAge());
        assertEquals("US", importedEntities.get(0).getReviewerCountry());
        assertEquals(LocalDate.of(2025, 3,24), importedEntities.get(0).getCreateDate());
    }

    @Test
    void shouldNotImportDataFile() {
        when(repository.findAllImportedDates()).thenReturn(Set.of(LocalDate.of(2025, 3,24)));

        service.importData();

        verify(repository, times(0)).saveAll(any());
    }

}
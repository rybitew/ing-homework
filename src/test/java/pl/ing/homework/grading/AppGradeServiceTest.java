package pl.ing.homework.grading;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.ing.homework.grading.dto.TopAppGradeDto;
import pl.ing.homework.grading.model.AgeGroup;
import pl.ing.homework.grading.model.TopAppGradeImpl;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AppGradeServiceTest {

    AppGradeService service;
    @Mock
    AppGradeRepository repository;

    @BeforeAll
    void setup() {
        service = new AppGradeService(repository);
    }

    @BeforeEach
    void resetMocks() {
        reset(repository);
    }

    @Test
    void shouldReturnDoubleAsAverage() {
        when(repository.findAverageBetweenDates(any(), any(), any())).thenReturn(4.0);

        Double avg = service.getAverage(UUID.randomUUID(), LocalDate.now(), LocalDate.now().plusMonths(1));

        assertEquals(4.0, avg);
    }

    @Test
    void shouldReturnNullAsAverage() {
        when(repository.findAverageBetweenDates(any(), any(), any())).thenReturn(null);

        Double avg = service.getAverage(UUID.randomUUID(), LocalDate.now(), LocalDate.now().plusMonths(1));

        assertNull(avg);
    }

    @Test
    void shouldReturnEmptyList() {
        when(repository.findByAgeGroupBetweenDates(any(), any(), any(), any())).thenReturn(Collections.emptyList());

        List<TopAppGradeDto> result = service
                .getTop100InAgeGroup(AgeGroup.AGE_GROUP_1, LocalDate.now(), LocalDate.now().plusMonths(1));

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnData() {
        List<TopAppGradeDto> result = List.of(
                new TopAppGradeImpl().name("TestName1").appId(UUID.randomUUID()),
                new TopAppGradeImpl().name("TestName2").appId(UUID.randomUUID()),
                new TopAppGradeImpl().name("TestName3").appId(UUID.randomUUID())
        );
        when(repository.findByAgeGroupBetweenDates(any(), any(), any(), any())).thenReturn(result);

        List<TopAppGradeDto> topAppGradeDtos = service
                .getTop100InAgeGroup(AgeGroup.AGE_GROUP_1, LocalDate.now(), LocalDate.now().plusMonths(1));

        assertEquals(3, topAppGradeDtos.size());
        assertEquals(result, topAppGradeDtos);
    }

    @Test
    void shouldCallFindByAgeGroupBetweenDates() {
        when(repository.findByAgeGroupBetweenDates(any(), any(), any(), any())).thenReturn(Collections.emptyList());

        service.getTop100InAgeGroup(AgeGroup.AGE_GROUP_1, LocalDate.now(), LocalDate.now().plusMonths(1));

        verify(repository, times(1)).findByAgeGroupBetweenDates(any(), any(), any(), any());
        verify(repository, times(0)).findByOlderThanAgeBetweenDates(any(), any(), any());
    }

    @Test
    void shouldCallFindByOlderThanAgeBetweenDates() {
        when(repository.findByOlderThanAgeBetweenDates(any(), any(), any())).thenReturn(Collections.emptyList());

        service.getTop100InAgeGroup(AgeGroup.AGE_GROUP_7, LocalDate.now(), LocalDate.now().plusMonths(1));

        verify(repository, times(1)).findByOlderThanAgeBetweenDates(any(), any(), any());
        verify(repository, times(0)).findByAgeGroupBetweenDates(any(), any(), any(), any());
    }
}
package pl.ing.homework.grading;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.ing.homework.grading.model.AppGradeEntity;
import pl.ing.homework.grading.model.TopAppGradeDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface AppGradeRepository extends CrudRepository<AppGradeEntity, UUID> {
    @Query("select avg(a.rating) from AppGradeEntity a where a.id = ?1 and a.createDate between ?2 and ?3")
    Double findAverageBetweenDates(UUID appId, LocalDate since, LocalDate until);

    @Query("select a.name as name, a.id as id from AppGradeEntity a " +
            "where a.reviewerAge between ?1 and ?2 and a.createDate between ?3 and ?4")
    List<TopAppGradeDto> findByAgeGroupBetweenDates(Integer ageFloor, Integer ageCeiling, LocalDate since,
                                                    LocalDate until);

    @Query("select a.name as name, a.id as id from AppGradeEntity a " +
            "where a.reviewerAge > ?1 and a.createDate between ?2 and ?3")
    List<TopAppGradeDto> findByOlderThanAgeBetweenDates(Integer age, LocalDate since, LocalDate until);

    @Query("select distinct a.createDate from AppGradeEntity a")
    Set<LocalDate> findAllImportedDates();
}

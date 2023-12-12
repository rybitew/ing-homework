package pl.ing.homework.grading;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;
import pl.ing.homework.grading.model.AppGradeEntity;
import pl.ing.homework.grading.model.MonthlyAppRating;
import pl.ing.homework.grading.model.TopAppGradeDto;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface AppGradeRepository extends CrudRepository<AppGradeEntity, UUID> {
    @Query("select avg(a.rating) from AppGradeEntity a where a.appId = ?1 and a.createDate between ?2 and ?3")
    Double findAverageBetweenDates(UUID appId, LocalDate since, LocalDate until);

    @Query("select a.name as name, a.appId as appId from AppGradeEntity a " +
            "where a.reviewerAge between ?1 and ?2 and a.createDate between ?3 and ?4 " +
            "order by a.rating desc " +
            "limit 100")
    List<TopAppGradeDto> findByAgeGroupBetweenDates(Integer ageFloor, Integer ageCeiling, LocalDate since,
                                                    LocalDate until);

    @Query("select a.name as name, a.appId as appId from AppGradeEntity a " +
            "where a.reviewerAge > ?1 and a.createDate between ?2 and ?3 " +
            "order by a.rating desc " +
            "limit 100")
    List<TopAppGradeDto> findByOlderThanAgeBetweenDates(Integer age, LocalDate since, LocalDate until);

    @Query("select distinct a.createDate from AppGradeEntity a")
    Set<LocalDate> findAllImportedDates();

    @Query("""
            select a.name as appName, a.appId as appId, avg(a.rating) as ratingThisMonth,
                old.oldAvg as ratingPreviousMonth
            from AppGradeEntity a join (
                select b.appId as appId, avg(b.rating) as oldAvg
                from AppGradeEntity b
                where year(b.createDate) = ?1 and month(b.createDate) = ?2 - 1
                group by b.appId
            ) old on old.appId = a.appId
            where year(a.createDate) = ?1 and month(a.createDate) = ?2
            group by a.appId, a.name, oldAvg
            having (oldAvg - avg(a.rating)) < 0
            order by avg(a.rating) - oldAvg desc
            limit 100
            """)
    List<MonthlyAppRating> findTopTrendingByMonth(Integer year, Integer month);

    @Query("""
            select a.name as appName, a.appId as appId, avg(a.rating) as ratingThisMonth,
                old.oldAvg as ratingPreviousMonth
            from AppGradeEntity a join (
                select b.appId as appId, avg(b.rating) as oldAvg
                from AppGradeEntity b
                where year(b.createDate) = ?1 and month(b.createDate) = ?2 - 1
                group by b.appId
            ) old on old.appId = a.appId
            where year(a.createDate) = ?1 and month(a.createDate) = ?2
            group by a.appId, a.name, oldAvg
            having (oldAvg - avg(a.rating)) >= 0.3
            order by oldAvg - avg(a.rating) desc
            limit 100
            """)
    List<MonthlyAppRating> findDecliningAppsByMonth(Integer year, Integer month);
}

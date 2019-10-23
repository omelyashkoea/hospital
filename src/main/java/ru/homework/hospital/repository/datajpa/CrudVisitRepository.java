package ru.homework.hospital.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.homework.hospital.model.Visit;

import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudVisitRepository extends JpaRepository<Visit, Integer> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Visit v WHERE v.id=:id AND v.user.id=:userId")
    int delete(@Param("id") int id, @Param("userId") int userId);

    @Override
    @Transactional
    Visit save(Visit item);

    @Query("SELECT v FROM Visit v WHERE v.user.id=:userId ORDER BY v.dateTime DESC")
    List<Visit> getAll(@Param("userId") int userId);

    @SuppressWarnings("JpaQlInspection")
    @Query("SELECT v from Visit v WHERE v.user.id=:userId AND v.dateTime BETWEEN :startDate AND :endDate ORDER BY v.dateTime DESC")
    List<Visit> getBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("userId") int userId);

    @Query("SELECT v FROM Visit v JOIN FETCH v.user WHERE v.id = ?1 and v.user.id = ?2")
    Visit getWithUser(int id, int userId);
}
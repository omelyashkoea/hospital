package ru.homework.hospital.repository;

import ru.homework.hospital.model.Visit;

import java.time.LocalDateTime;
import java.util.List;

public interface VisitRepository {
    // null if updated visit do not belong to userId
    Visit save(Visit visit, int userId);

    // false if visit do not belong to userId
    boolean delete(int id, int userId);

    // null if visit do not belong to userId
    Visit get(int id, int userId);

    // ORDERED dateTime desc
    List<Visit> getAll(int userId);

    // ORDERED dateTime desc
    List<Visit> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId);

    default Visit getWithUser(int id, int userId) {
        throw new UnsupportedOperationException();
    }
}

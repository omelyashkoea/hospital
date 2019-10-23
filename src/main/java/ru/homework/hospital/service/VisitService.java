package ru.homework.hospital.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.homework.hospital.model.Visit;
import ru.homework.hospital.util.DateTimeUtil;
import ru.homework.hospital.repository.VisitRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static ru.homework.hospital.util.ValidationUtil.checkNotFoundWithId;

@Service
public class VisitService {

    private final VisitRepository repository;

    @Autowired
    public VisitService(VisitRepository repository) {
        this.repository = repository;
    }

    public Visit get(int id, int userId) {
        return checkNotFoundWithId(repository.get(id, userId), id);
    }

    public void delete(int id, int userId) {
        checkNotFoundWithId(repository.delete(id, userId), id);
    }

    public List<Visit> getBetweenDates(@Nullable LocalDate startDate, @Nullable LocalDate endDate, int userId) {
        return getBetweenDateTimes(DateTimeUtil.adjustStartDateTime(startDate), DateTimeUtil.adjustEndDateTime(endDate), userId);
    }

    private List<Visit> getBetweenDateTimes(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        Assert.notNull(startDateTime, "startDateTime must not be null");
        Assert.notNull(endDateTime, "endDateTime  must not be null");
        return repository.getBetween(startDateTime, endDateTime, userId);
    }

    public List<Visit> getAll(int userId) {
        return repository.getAll(userId);
    }

    public void update(Visit visit, int userId) {
        Assert.notNull(visit, "visit must not be null");
        checkNotFoundWithId(repository.save(visit, userId), visit.getId());
    }

    public Visit create(Visit visit, int userId) {
        Assert.notNull(visit, "visit must not be null");
        return repository.save(visit, userId);
    }

    public Visit getWithUser(int id, int userId) {
        return checkNotFoundWithId(repository.getWithUser(id, userId), id);
    }
}
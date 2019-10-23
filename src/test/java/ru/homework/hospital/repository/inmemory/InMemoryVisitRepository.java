package ru.homework.hospital.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.homework.hospital.model.Visit;
import ru.homework.hospital.repository.VisitRepository;
import ru.homework.hospital.util.Util;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryVisitRepository implements VisitRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryVisitRepository.class);

    // Map  userId -> (visitId-> visit)
    private Map<Integer, InMemoryBaseRepository<Visit>> usersVisitsMap = new ConcurrentHashMap<>();

    @Override
    public Visit save(Visit visit, int userId) {
        Objects.requireNonNull(visit, "visit must not be null");
        var visits = usersVisitsMap.computeIfAbsent(userId, uid -> new InMemoryBaseRepository<>());
        return visits.save(visit);
    }

    @PostConstruct
    public void postConstruct() {
        log.info("+++ PostConstruct");
    }

    @PreDestroy
    public void preDestroy() {
        log.info("+++ PreDestroy");
    }

    @Override
    public boolean delete(int id, int userId) {
        var visits = usersVisitsMap.get(userId);
        return visits != null && visits.delete(id);
    }

    @Override
    public Visit get(int id, int userId) {
        var visits = usersVisitsMap.get(userId);
        return visits == null ? null : visits.get(id);
    }

    @Override
    public List<Visit> getAll(int userId) {
        return getAllFiltered(userId, visit -> true);
    }

    @Override
    public List<Visit> getBetween(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        Objects.requireNonNull(startDateTime, "startDateTime must not be null");
        Objects.requireNonNull(endDateTime, "endDateTime must not be null");
        return getAllFiltered(userId, visit -> Util.isBetween(visit.getDateTime(), startDateTime, endDateTime));
    }

    private List<Visit> getAllFiltered(int userId, Predicate<Visit> filter) {
        var visits = usersVisitsMap.get(userId);
        return visits == null ? Collections.emptyList() :
                visits.getCollection().stream()
                        .filter(filter)
                        .sorted(Comparator.comparing(Visit::getDateTime).reversed())
                        .collect(Collectors.toList());
    }
}
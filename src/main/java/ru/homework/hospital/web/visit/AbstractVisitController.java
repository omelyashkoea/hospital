package ru.homework.hospital.web.visit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.homework.hospital.to.VisitTo;
import ru.homework.hospital.web.SecurityUtil;
import ru.homework.hospital.model.Visit;
import ru.homework.hospital.service.VisitService;
import ru.homework.hospital.util.VisitsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.homework.hospital.util.ValidationUtil.assureIdConsistent;
import static ru.homework.hospital.util.ValidationUtil.checkNew;

public abstract class AbstractVisitController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private VisitService service;

    public Visit get(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("get visit {} for user {}", id, userId);
        return service.get(id, userId);
    }

    public void delete(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("delete visit {} for user {}", id, userId);
        service.delete(id, userId);
    }

    public List<VisitTo> getAll() {
        int userId = SecurityUtil.authUserId();
        log.info("getAll for user {}", userId);
        return VisitsUtil.getWithExpert(service.getAll(userId), SecurityUtil.authUserExpert());
    }

    public Visit create(Visit visit) {
        int userId = SecurityUtil.authUserId();
        checkNew(visit);
        log.info("create {} for user {}", visit, userId);
        return service.create(visit, userId);
    }

    public void update(Visit visit, int id) {
        int userId = SecurityUtil.authUserId();
        assureIdConsistent(visit, id);
        log.info("update {} for user {}", visit, userId);
        service.update(visit, userId);
    }

    /**
     * <ol>Filter separately
     * <li>by date</li>
     * <li>by time for every date</li>
     * </ol>
     */
    public List<VisitTo> getBetween(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        int userId = SecurityUtil.authUserId();
        log.info("getBetween dates({} - {}) time({} - {}) for user {}", startDate, endDate, startTime, endTime, userId);

        List<Visit> visitsDateFiltered = service.getBetweenDates(startDate, endDate, userId);
        return VisitsUtil.getFilteredWithExpert(visitsDateFiltered, SecurityUtil.authUserExpert(), startTime, endTime);
    }
}
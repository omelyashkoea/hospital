package ru.homework.hospital.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.homework.hospital.UserTestData;
import ru.homework.hospital.VisitTestData;
import ru.homework.hospital.model.Visit;
import ru.homework.hospital.util.exception.ErrorType;
import ru.homework.hospital.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.time.Month;

import static java.time.LocalDateTime.of;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.homework.hospital.VisitTestData.*;

public abstract class AbstractVisitServiceTest extends AbstractServiceTest {

    @Autowired
    protected VisitService service;

    @Test
    void delete() throws Exception {
        service.delete(VisitTestData.VISIT1_ID, UserTestData.USER_ID);
        assertMatch(service.getAll(UserTestData.USER_ID), VisitTestData.VISIT_USER_4, VisitTestData.VISIT_USER_3, VisitTestData.VISIT_USER_2);
    }

    @Test
    void deleteNotFound() throws Exception {
        assertThrows(NotFoundException.class, () ->
                service.delete(1, UserTestData.USER_ID));
    }

    @Test
    void deleteNotOwn() throws Exception {
        assertThrows(NotFoundException.class, () ->
                service.delete(VisitTestData.VISIT1_ID, UserTestData.ADMIN_ID));
    }

    @Test
    void create() throws Exception {
        Visit newVisit = VisitTestData.getCreated();
        Visit created = service.create(newVisit, UserTestData.USER_ID);
        newVisit.setId(created.getId());
        VisitTestData.assertMatch(newVisit, created);
        assertMatch(service.getAll(UserTestData.USER_ID), newVisit, VisitTestData.VISIT_USER_4, VisitTestData.VISIT_USER_3, VisitTestData.VISIT_USER_2, VisitTestData.VISIT_USER_1);
    }

    @Test
    void get() throws Exception {
        Visit actual = service.get(VisitTestData.VISIT1_ID + 2, UserTestData.USER_2_ID);
        VisitTestData.assertMatch(actual, VisitTestData.VISIT_USER2_1);
    }

    @Test
    void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () ->
                service.get(1, UserTestData.ADMIN_ID));
    }

    @Test
    void getNotOwn() throws Exception {
        assertThrows(NotFoundException.class, () ->
                service.get(VisitTestData.VISIT1_ID, UserTestData.ADMIN_ID));
    }

    @Test
    void update() throws Exception {
        Visit updated = VisitTestData.getUpdated();
        service.update(updated, UserTestData.USER_ID);
        assertMatch(service.get(VisitTestData.VISIT1_ID, UserTestData.USER_ID), updated);
    }

    @Test
    void updateNotFound() throws Exception {
        NotFoundException e = assertThrows(NotFoundException.class, () -> service.update(VisitTestData.VISIT_USER_1, UserTestData.ADMIN_ID));
        String msg = e.getMessage();
        assertTrue(msg.contains(ErrorType.DATA_NOT_FOUND.name()));
        assertTrue(msg.contains(NotFoundException.NOT_FOUND_EXCEPTION));
        assertTrue(msg.contains(String.valueOf(VisitTestData.VISIT1_ID)));
    }

    @Test
    void getAll() throws Exception {
        assertMatch(service.getAll(UserTestData.USER_ID), VisitTestData.VISITS);
    }

    @Test
    void getBetween() throws Exception {
        assertMatch(service.getBetweenDates(
                LocalDate.of(2015, Month.MAY, 30),
                LocalDate.of(2015, Month.MAY, 30), UserTestData.USER_ID), VisitTestData.VISIT_USER_2, VisitTestData.VISIT_USER_1);
    }

    @Test
    void createWithException() throws Exception {
        validateRootCause(() -> service.create(new Visit(null, of(2015, Month.JUNE, 1, 18, 0), "  ", "Терапевт"), UserTestData.USER_ID), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new Visit(null, null, "Description", "Невролог"), UserTestData.USER_ID), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new Visit(null, of(2015, Month.JUNE, 1, 18, 0), "Description", "Врач"), UserTestData.USER_ID), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new Visit(null, of(2015, Month.JUNE, 1, 18, 0), "Description", "Гинеколог1".repeat(500)), UserTestData.USER_ID), ConstraintViolationException.class);
    }
}
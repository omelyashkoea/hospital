package ru.homework.hospital.service.datajpa;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.homework.hospital.Profiles;
import ru.homework.hospital.UserTestData;
import ru.homework.hospital.VisitTestData;
import ru.homework.hospital.service.AbstractVisitServiceTest;
import ru.homework.hospital.model.Visit;
import ru.homework.hospital.util.exception.NotFoundException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles(Profiles.DATAJPA)
class DataJpaVisitServiceTest extends AbstractVisitServiceTest {
    @Test
    void getWithUser() throws Exception {
        Visit adminVisit = service.getWithUser(VisitTestData.VISIT1_ID + 2, UserTestData.USER_2_ID);
        VisitTestData.assertMatch(adminVisit, VisitTestData.VISIT_USER2_1);
        UserTestData.assertMatch(adminVisit.getUser(), UserTestData.USER_2);
    }

    @Test
    void getWithUserNotFound() throws Exception {
        assertThrows(NotFoundException.class, () ->
                service.getWithUser(VisitTestData.VISIT1_ID, UserTestData.ADMIN_ID));
    }
}

package ru.homework.hospital.service.datajpa;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.homework.hospital.Profiles;
import ru.homework.hospital.UserTestData;
import ru.homework.hospital.VisitTestData;
import ru.homework.hospital.service.AbstractUserServiceTest;
import ru.homework.hospital.model.User;
import ru.homework.hospital.util.exception.NotFoundException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles(Profiles.DATAJPA)
class DataJpaUserServiceTest extends AbstractUserServiceTest {
    @Test
    void getWithVisits() throws Exception {
        User admin = service.getWithVisits(UserTestData.USER_2_ID);
        UserTestData.assertMatch(admin, UserTestData.USER_2);
        VisitTestData.assertMatch(admin.getVisits(), VisitTestData.VISIT_USER2_4, VisitTestData.VISIT_USER2_3, VisitTestData.VISIT_USER2_2, VisitTestData.VISIT_USER2_1);
    }

    @Test
    void getWithVisitsNotFound() throws Exception {
        assertThrows(NotFoundException.class, () ->
                service.getWithVisits(1));
    }
}
package ru.homework.hospital.service.jpa;

import org.springframework.test.context.ActiveProfiles;
import ru.homework.hospital.Profiles;
import ru.homework.hospital.service.AbstractUserServiceTest;

@ActiveProfiles(Profiles.JPA)
class JpaUserServiceTest extends AbstractUserServiceTest {
}
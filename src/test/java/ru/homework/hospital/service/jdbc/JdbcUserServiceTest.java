package ru.homework.hospital.service.jdbc;

import org.springframework.test.context.ActiveProfiles;
import ru.homework.hospital.Profiles;
import ru.homework.hospital.service.AbstractUserServiceTest;

@ActiveProfiles(Profiles.JDBC)
class JdbcUserServiceTest extends AbstractUserServiceTest {
}
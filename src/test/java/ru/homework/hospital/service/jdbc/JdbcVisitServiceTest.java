package ru.homework.hospital.service.jdbc;

import org.springframework.test.context.ActiveProfiles;
import ru.homework.hospital.Profiles;
import ru.homework.hospital.service.AbstractVisitServiceTest;

@ActiveProfiles(Profiles.JDBC)
class JdbcVisitServiceTest extends AbstractVisitServiceTest {
}
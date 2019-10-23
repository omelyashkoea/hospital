package ru.homework.hospital.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.dao.DataAccessException;
import org.springframework.util.Assert;
import ru.homework.hospital.UserTestData;
import ru.homework.hospital.model.Role;
import ru.homework.hospital.model.User;
import ru.homework.hospital.repository.JpaUtil;
import ru.homework.hospital.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractUserServiceTest extends AbstractServiceTest {

    @Autowired
    protected UserService service;

    @Autowired
    private CacheManager cacheManager;

    private JpaUtil jpaUtil;

    @Autowired(required = false)
    private void setJpaUtil(JpaUtil jpaUtil) {
        Assert.isTrue(!isJpaBased() || jpaUtil != null, "JpaUtil is missed in JPA profile");
        this.jpaUtil = jpaUtil;
    }

    @BeforeEach
    void setUp() throws Exception {
        cacheManager.getCache("users").clear();
        if (isJpaBased()) {
            jpaUtil.clear2ndLevelHibernateCache();
        }
    }

    @Test
    void create() throws Exception {
        User newUser = new User(null, "New", "new@gmail.com", "newPass", "Хирург", false, new Date(), Collections.singleton(Role.ROLE_USER));
        User created = service.create(new User(newUser));
        newUser.setId(created.getId());
        UserTestData.assertMatch(created, newUser);
        UserTestData.assertMatch(service.getAll(), UserTestData.ADMIN, newUser, UserTestData.USER, UserTestData.USER_2);
    }

    @Test
    void duplicateMailCreate() throws Exception {
        assertThrows(DataAccessException.class, () ->
                service.create(new User(null, "Duplicate", "user@yandex.ru", "newPass", "newExpert", Role.ROLE_USER)));
    }

    @Test
    void delete() throws Exception {
        service.delete(UserTestData.USER_ID);
        UserTestData.assertMatch(service.getAll(), UserTestData.ADMIN, UserTestData.USER_2);
    }

    @Test
    void deletedNotFound() throws Exception {
        assertThrows(NotFoundException.class, () ->
                service.delete(1));
    }

    @Test
    void get() throws Exception {
        User user = service.get(UserTestData.ADMIN_ID);
        UserTestData.assertMatch(user, UserTestData.ADMIN);
    }

    @Test
    void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () ->
                service.get(1));
    }

    @Test
    void getByEmail() throws Exception {
        User user = service.getByEmail("admin@gmail.com");
        UserTestData.assertMatch(user, UserTestData.ADMIN);
    }

    @Test
    void update() throws Exception {
        User updated = new User(UserTestData.USER);
        updated.setName("UpdatedName");
        updated.setExpert("updateExpert");
        updated.setRoles(Collections.singletonList(Role.ROLE_ADMIN));
        service.update(new User(updated));
        UserTestData.assertMatch(service.get(UserTestData.USER_ID), updated);
    }

    @Test
    void getAll() throws Exception {
        List<User> all = service.getAll();
        UserTestData.assertMatch(all, UserTestData.ADMIN, UserTestData.USER, UserTestData.USER_2);
    }

    @Test
    void enable() {
        service.enable(UserTestData.USER_ID, false);
        assertFalse(service.get(UserTestData.USER_ID).isEnabled());
        service.enable(UserTestData.USER_ID, true);
        assertTrue(service.get(UserTestData.USER_ID).isEnabled());
    }

    @Test
    void createWithException() throws Exception {
        validateRootCause(() -> service.create(new User(null, "  ", "mail@yandex.ru", "password", "Педиатр", Role.ROLE_USER)), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new User(null, "User", "  ", "password", "user", Role.ROLE_USER)), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new User(null, "User", "mail@yandex.ru", "  ", "Отоларинголог", Role.ROLE_USER)), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new User(null, "User", "mail@yandex.ru", "password", "Врач", true, new Date(), Collections.emptySet())), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new User(null, "User", "mail@yandex.ru", "password", "Гинеколог1", true, new Date(), Collections.emptySet())), ConstraintViolationException.class);
    }
}
package ru.homework.hospital.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.homework.hospital.UserTestData;
import ru.homework.hospital.model.User;
import ru.homework.hospital.repository.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Repository
public class InMemoryUserRepository extends InMemoryBaseRepository<User> implements UserRepository {

    public void init() {
        entryMap.clear();
        entryMap.put(UserTestData.USER_ID, UserTestData.USER);
        entryMap.put(UserTestData.ADMIN_ID, UserTestData.ADMIN);
    }

    @Override
    public List<User> getAll() {
        return getCollection().stream()
                .sorted(Comparator.comparing(User::getName).thenComparing(User::getEmail))
                .collect(Collectors.toList());
    }

    @Override
    public User getByEmail(String email) {
        Objects.requireNonNull(email, "email must not be null");
        return getCollection().stream()
                .filter(u -> email.equals(u.getEmail()))
                .findFirst()
                .orElse(null);
    }
}
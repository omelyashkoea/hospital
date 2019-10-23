package ru.homework.hospital;

import org.springframework.test.context.ActiveProfilesResolver;

public class AllActiveProfileResolver implements ActiveProfilesResolver {

    @Override
    public String[] resolve(Class<?> aClass) {
        return new String[]{Profiles.REPOSITORY_IMPLEMENTATION, Profiles.getActiveDbProfile()};
    }
}
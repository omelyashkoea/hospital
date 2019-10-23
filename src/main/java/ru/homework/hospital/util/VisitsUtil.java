package ru.homework.hospital.util;

import org.springframework.lang.Nullable;
import ru.homework.hospital.to.VisitTo;
import ru.homework.hospital.model.Visit;

import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class VisitsUtil {

    private VisitsUtil() {
    }

    public static List<VisitTo> getWithExpert(Collection<Visit> visits, String expert) {
        return getFilteredWithExpert(visits, visit -> true);
    }

    public static List<VisitTo> getFilteredWithExpert(Collection<Visit> visits, String expert, @Nullable LocalTime startTime, @Nullable LocalTime endTime) {
        return getFilteredWithExpert(visits, visit -> Util.isBetween(visit.getTime(), startTime, endTime) && expert.contains(visit.getExpert()));
    }

    private static List<VisitTo> getFilteredWithExpert(Collection<Visit> visits, Predicate<Visit> filter) {
        return visits.stream()
                .filter(filter)
                .map(visit -> createWithExcess(visit, false))
                .collect(Collectors.toList());
    }

    public static VisitTo createWithExcess(Visit visit, boolean excess) {
        return new VisitTo(visit.getId(), visit.getDateTime(), visit.getDescription(), visit.getExpert(), excess);
    }
}
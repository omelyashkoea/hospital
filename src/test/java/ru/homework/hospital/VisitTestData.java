package ru.homework.hospital;

import org.springframework.test.web.servlet.ResultMatcher;
import ru.homework.hospital.model.Visit;
import ru.homework.hospital.to.VisitTo;

import java.time.Month;
import java.util.List;

import static java.time.LocalDateTime.of;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.homework.hospital.model.AbstractBaseEntity.START_SEQ;

public class VisitTestData {
    public static final int VISIT1_ID = START_SEQ + 3;
    public static final int ADMIN_VISIT_ID = START_SEQ + 11;

    public static final Visit VISIT_USER_1 = new Visit(VISIT1_ID, of(2015, Month.MAY, 30, 10, 0), "Ванюков А.М.", "Терапевт");
    public static final Visit VISIT_USER_2 = new Visit(VISIT1_ID + 1, of(2015, Month.MAY, 30, 13, 0), "Петров Ю.А.", "Терапевт");
    public static final Visit VISIT_USER2_1 = new Visit(VISIT1_ID + 2, of(2015, Month.MAY, 30, 20, 0), "Галыгин П.Р.", "Невролог");
    public static final Visit VISIT_USER2_2 = new Visit(VISIT1_ID + 3, of(2015, Month.MAY, 31, 10, 0), "Незлобин Г.Б.", "Невролог");
    public static final Visit VISIT_USER2_3 = new Visit(VISIT1_ID + 4, of(2015, Month.MAY, 31, 13, 0), "Незлобин Г.Б.", "Невролог");
    public static final Visit VISIT_USER2_4 = new Visit(VISIT1_ID + 5, of(2015, Month.MAY, 31, 20, 0), "Пушкин А.С.", "Невролог");
    public static final Visit VISIT_USER_3 = new Visit(VISIT1_ID + 6, of(2015, Month.JUNE, 1, 14, 0), "Рыжов Н.А.", "Терапевт");
    public static final Visit VISIT_USER_4 = new Visit(VISIT1_ID + 7, of(2015, Month.JUNE, 1, 21, 0), "Петров Ю.А.", "Терапевт");
    public static final Visit ADMIN_VISIT1 = new Visit(ADMIN_VISIT_ID, of(2015, Month.JUNE, 1, 14, 0), "Врач-невролог", "Админ");

    public static final List<Visit> VISITS = List.of(VISIT_USER_4, VISIT_USER_3, VISIT_USER_2, VISIT_USER_1);

    public static Visit getCreated() {
        return new Visit(null, of(2015, Month.JUNE, 1, 22, 0), "Созданный ужин", "Терапевт");
    }

    public static Visit getUpdated() {
        return new Visit(VISIT1_ID, VISIT_USER_1.getDateTime(), "updatePacient", "Невролог");
    }

    public static void assertMatch(Visit actual, Visit expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "user");
    }

    public static void assertMatch(Iterable<Visit> actual, Visit... expected) {
        assertMatch(actual, List.of(expected));
    }

    public static void assertMatch(Iterable<Visit> actual, Iterable<Visit> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("user").isEqualTo(expected);
    }

    public static ResultMatcher contentJson(VisitTo... expected) {
        return contentJson(List.of(expected));
    }

    public static ResultMatcher contentJson(Iterable<VisitTo> expected) {
        return result -> assertThat(TestUtil.readListFromJsonMvcResult(result, VisitTo.class)).isEqualTo(expected);
    }
}

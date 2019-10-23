package ru.homework.hospital.web.json;

import org.junit.jupiter.api.Test;
import ru.homework.hospital.UserTestData;
import ru.homework.hospital.VisitTestData;
import ru.homework.hospital.model.Visit;
import ru.homework.hospital.model.User;

import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonUtilTest {

    @Test
    void readWriteValue() throws Exception {
        String json = JsonUtil.writeValue(VisitTestData.ADMIN_VISIT1);
        System.out.println(json);
        Visit meal = JsonUtil.readValue(json, Visit.class);
        VisitTestData.assertMatch(meal, VisitTestData.ADMIN_VISIT1);
    }

    @Test
    void readWriteValues() throws Exception {
        String json = JsonUtil.writeValue(VisitTestData.VISITS);
        System.out.println(json);
        List<Visit> visits = JsonUtil.readValues(json, Visit.class);
        VisitTestData.assertMatch(visits, VisitTestData.VISITS);
    }

    @Test
    void writeOnlyAccess() throws Exception {
        String json = JsonUtil.writeValue(UserTestData.USER);
        System.out.println(json);
        assertThat(json, not(containsString("password")));
        String jsonWithPass = UserTestData.jsonWithPassword(UserTestData.USER, "newPass");
        System.out.println(jsonWithPass);
        User user = JsonUtil.readValue(jsonWithPass, User.class);
        assertEquals(user.getPassword(), "newPass");
    }
}
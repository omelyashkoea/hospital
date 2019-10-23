package ru.homework.hospital.web.user;

import org.junit.jupiter.api.Test;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.homework.hospital.Profiles;
import ru.homework.hospital.TestUtil;
import ru.homework.hospital.UserTestData;
import ru.homework.hospital.web.AbstractControllerTest;
import ru.homework.hospital.util.exception.ErrorType;

import java.io.IOException;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.homework.hospital.util.exception.ModificationRestrictionException.EXCEPTION_MODIFICATION_RESTRICTION;

@ActiveProfiles({Profiles.HEROKU})
class HerokuRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = AdminRestController.REST_URL + '/';

    // Set DATABASE_URL environment for heroku profile
    static {
        Resource resource = new ClassPathResource("db/postgres.properties");
        try {
            PropertySource propertySource = new ResourcePropertySource(resource);
            String herokuDbUrl = String.format("postgres://%s:%s@%s",
                    propertySource.getProperty("database.username"),
                    propertySource.getProperty("database.password"),
                    ((String) propertySource.getProperty("database.url")).substring(18));
            System.out.println(herokuDbUrl);

            System.setProperty("DATABASE_URL", herokuDbUrl);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL + UserTestData.USER_ID)
                .with(TestUtil.userHttpBasic(UserTestData.ADMIN)))
                .andDo(print())
                .andExpect(errorType(ErrorType.APP_ERROR))
                .andExpect(detailMessage(EXCEPTION_MODIFICATION_RESTRICTION))
                .andExpect(status().isUnavailableForLegalReasons());
    }

    @Test
    void update() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + UserTestData.USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(TestUtil.userHttpBasic(UserTestData.ADMIN))
                .content(UserTestData.jsonWithPassword(UserTestData.USER, "password")))
                .andExpect(errorType(ErrorType.APP_ERROR))
                .andExpect(detailMessage(EXCEPTION_MODIFICATION_RESTRICTION))
                .andExpect(status().isUnavailableForLegalReasons());
    }
}
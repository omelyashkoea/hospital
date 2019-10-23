package ru.homework.hospital.web;

import org.junit.jupiter.api.Test;
import ru.homework.hospital.TestUtil;
import ru.homework.hospital.UserTestData;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RootControllerTest extends AbstractControllerTest {

    @Test
    void getUsers() throws Exception {
        mockMvc.perform(get("/users")
                .with(TestUtil.userAuth(UserTestData.ADMIN)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/users.jsp"));
    }

    @Test
    void unAuth() throws Exception {
        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    void getVisits() throws Exception {
        mockMvc.perform(get("/visits")
                .with(TestUtil.userAuth(UserTestData.USER)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("visits"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/visits.jsp"));
    }
}
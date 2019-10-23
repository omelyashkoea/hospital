package ru.homework.hospital.web.visit;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.homework.hospital.TestUtil;
import ru.homework.hospital.UserTestData;
import ru.homework.hospital.VisitTestData;
import ru.homework.hospital.model.Visit;
import ru.homework.hospital.service.VisitService;
import ru.homework.hospital.web.AbstractControllerTest;
import ru.homework.hospital.web.json.JsonUtil;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.homework.hospital.VisitTestData.assertMatch;
import static ru.homework.hospital.VisitTestData.contentJson;
import static ru.homework.hospital.model.AbstractBaseEntity.START_SEQ;
import static ru.homework.hospital.util.VisitsUtil.getWithExpert;
import static ru.homework.hospital.util.exception.ErrorType.VALIDATION_ERROR;
import static ru.homework.hospital.web.ExceptionInfoHandler.EXCEPTION_DUPLICATE_DATETIME;

class VisitRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = VisitRestController.REST_URL + '/';

    @Autowired
    private VisitService service;

    @Test
    void getError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + UserTestData.USER_2_ID)
                .with(TestUtil.userHttpBasic(UserTestData.USER_2)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void getUnauth() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + VisitTestData.VISIT1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + VisitTestData.ADMIN_VISIT_ID)
                .with(TestUtil.userHttpBasic(UserTestData.USER)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL + VisitTestData.VISIT1_ID)
                .with(TestUtil.userHttpBasic(UserTestData.USER)))
                .andExpect(status().isNoContent());
        VisitTestData.assertMatch(service.getAll(START_SEQ), VisitTestData.VISIT_USER_4, VisitTestData.VISIT_USER_3, VisitTestData.VISIT_USER_2);
    }

    @Test
    void deleteNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL + VisitTestData.ADMIN_VISIT_ID)
                .with(TestUtil.userHttpBasic(UserTestData.USER)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void update() throws Exception {
        Visit updated = VisitTestData.getUpdated();

        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + VisitTestData.VISIT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated))
                .with(TestUtil.userHttpBasic(UserTestData.USER)))
                .andExpect(status().isNoContent());

        assertMatch(service.get(VisitTestData.VISIT1_ID, START_SEQ), updated);
    }

    @Test
    void createWithLocation() throws Exception {
        Visit created = VisitTestData.getCreated();
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(created))
                .with(TestUtil.userHttpBasic(UserTestData.ADMIN)));

        Visit returned = TestUtil.readFromJson(action, Visit.class);
        created.setId(returned.getId());

        VisitTestData.assertMatch(returned, created);
        assertMatch(service.getAll(UserTestData.ADMIN_ID), created);
    }

    @Test
    void getAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL)
                .with(TestUtil.userHttpBasic(UserTestData.USER)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(getWithExpert(VisitTestData.VISITS, UserTestData.USER.getExpert())));
    }

    @Test
    void filterAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "filter?startDate=&endTime=")
                .with(TestUtil.userHttpBasic(UserTestData.USER)))
                .andExpect(status().isOk())
                .andExpect(contentJson(getWithExpert(VisitTestData.VISITS, UserTestData.USER.getExpert())));
    }

    @Test
    void createInvalid() throws Exception {
        Visit invalid = new Visit(null, null, "Dummy", "Хирург");
        mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid))
                .with(TestUtil.userHttpBasic(UserTestData.ADMIN)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR))
                .andDo(print());
    }

    @Test
    void updateInvalid() throws Exception {
        Visit invalid = new Visit(VisitTestData.VISIT1_ID, null, null, "");
        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + VisitTestData.VISIT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid))
                .with(TestUtil.userHttpBasic(UserTestData.USER)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR))
                .andDo(print());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void updateDuplicate() throws Exception {
        Visit invalid = new Visit(VisitTestData.VISIT1_ID, VisitTestData.VISIT_USER_2.getDateTime(), "Dummy", "Терапевт");

        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + VisitTestData.VISIT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid))
                .with(TestUtil.userHttpBasic(UserTestData.USER)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(errorType(VALIDATION_ERROR))
                .andExpect(detailMessage(EXCEPTION_DUPLICATE_DATETIME));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createDuplicate() throws Exception {
        Visit invalid = new Visit(null, VisitTestData.VISIT_USER2_1.getDateTime(), "Dummy", "Онколог");
        mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid))
                .with(TestUtil.userHttpBasic(UserTestData.USER_2)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(errorType(VALIDATION_ERROR))
                .andExpect(detailMessage(EXCEPTION_DUPLICATE_DATETIME));
    }

    @Test
    void updateHtmlUnsafe() throws Exception {
        Visit invalid = new Visit(VisitTestData.VISIT1_ID, LocalDateTime.now(), "<script>alert(123)</script>", "newExpert");
        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + VisitTestData.VISIT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid))
                .with(TestUtil.userHttpBasic(UserTestData.USER)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR))
                .andDo(print());
    }
}
package ru.homework.hospital;

import org.springframework.context.support.GenericXmlApplicationContext;
import ru.homework.hospital.to.VisitTo;
import ru.homework.hospital.web.visit.VisitRestController;
import ru.homework.hospital.web.user.AdminRestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static ru.homework.hospital.UserTestData.USER;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 automatic resource management
        try (GenericXmlApplicationContext appCtx = new GenericXmlApplicationContext()) {
            appCtx.getEnvironment().setActiveProfiles(Profiles.getActiveDbProfile(), Profiles.REPOSITORY_IMPLEMENTATION);
            appCtx.load("spring/spring-app.xml", "spring/inmemory.xml");
            appCtx.refresh();

            TestUtil.mockAuthorize(USER);

            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            adminUserController.getAll();
            System.out.println();

            VisitRestController visitController = appCtx.getBean(VisitRestController.class);
            List<VisitTo> filteredVisitsWithExcess =
                    visitController.getBetween(
                            LocalDate.of(2015, Month.MAY, 30), LocalTime.of(7, 0),
                            LocalDate.of(2015, Month.MAY, 31), LocalTime.of(11, 0));
            filteredVisitsWithExcess.forEach(System.out::println);
        }
    }
}

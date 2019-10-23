package ru.homework.hospital.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;
import ru.homework.hospital.View;
import ru.homework.hospital.util.DateTimeUtil;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@SuppressWarnings("JpaQlInspection")
@NamedQueries({
        @NamedQuery(name = Visit.ALL_SORTED, query = "SELECT v FROM Visit v WHERE v.user.id=:userId ORDER BY v.dateTime DESC"),
        @NamedQuery(name = Visit.DELETE, query = "DELETE FROM Visit v WHERE v.id=:id AND v.user.id=:userId"),
        @NamedQuery(name = Visit.GET_BETWEEN, query = "SELECT v FROM Visit v " + "WHERE v.user.id=:userId AND v.dateTime BETWEEN :startDate AND :endDate ORDER BY v.dateTime DESC"),
})
@Entity
@Table(name = "visits", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "date_time"}, name = "visits_unique_user_datetime_idx")})
public class Visit extends AbstractBaseEntity {
    public static final String ALL_SORTED = "Visit.getAll";
    public static final String DELETE = "Visit.delete";
    public static final String GET_BETWEEN = "Visit.getBetween";

    @Column(name = "date_time", nullable = false)
    @NotNull
    @DateTimeFormat(pattern = DateTimeUtil.DATE_TIME_PATTERN)
    private LocalDateTime dateTime;

    @Column(name = "description", nullable = false)
    @NotBlank
    @Size(min = 2, max = 120)
    @SafeHtml(groups = {View.Web.class})
    private String description;

    @Column(name = "expert", nullable = false)
    @NotNull
    @Size(min = 5, max = 50)
    private String expert;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull(groups = View.Persist.class)
    private User user;

    public Visit() {
    }

    public Visit(LocalDateTime dateTime, String description, String expert) {
        this(null, dateTime, description, expert);
    }

    public Visit(Integer id, LocalDateTime dateTime, String description, String expert) {
        super(id);
        this.dateTime = dateTime;
        this.description = description;
        this.expert = expert;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public String getExpert() {
        return expert;
    }

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setExpert(String expert) {
        this.expert = expert;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Visit{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", expert=" + expert +
                '}';
    }
}

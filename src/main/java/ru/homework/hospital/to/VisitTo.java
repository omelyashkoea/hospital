package ru.homework.hospital.to;

import java.time.LocalDateTime;
import java.util.Objects;

public class VisitTo extends BaseTo {

    private LocalDateTime dateTime;

    private String description;

    private String expert;

    private boolean excess;

    public VisitTo() {
    }

    public VisitTo(Integer id, LocalDateTime dateTime, String description, String expert, boolean excess) {
        super(id);
        this.dateTime = dateTime;
        this.description = description;
        this.expert = expert;
        this.excess = excess;
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

    public boolean isExcess() {
        return excess;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VisitTo that = (VisitTo) o;
        return Objects.equals(expert,that.expert) &&
                excess == that.excess &&
                Objects.equals(id, that.id) &&
                Objects.equals(dateTime, that.dateTime) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateTime, description, expert, excess);
    }

    @Override
    public String toString() {
        return "VisitTo{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", expert=" + expert +
                ", excess=" + excess +
                '}';
    }
}
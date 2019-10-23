package ru.homework.hospital.repository.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.homework.hospital.model.User;
import ru.homework.hospital.model.Visit;
import ru.homework.hospital.repository.VisitRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaVisitRepository implements VisitRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Visit save(Visit visit, int userId) {
        if (!visit.isNew() && get(visit.getId(), userId) == null) {
            return null;
        }
        visit.setUser(em.getReference(User.class, userId));
        if (visit.isNew()) {
            em.persist(visit);
            return visit;
        } else {
            return em.merge(visit);
        }
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        return em.createNamedQuery(Visit.DELETE)
                .setParameter("id", id)
                .setParameter("userId", userId)
                .executeUpdate() != 0;
    }

    @Override
    public Visit get(int id, int userId) {
        Visit visit = em.find(Visit.class, id);
        return visit != null && visit.getUser().getId() == userId ? visit : null;
    }

    @Override
    public List<Visit> getAll(int userId) {
        return em.createNamedQuery(Visit.ALL_SORTED, Visit.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<Visit> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return em.createNamedQuery(Visit.GET_BETWEEN, Visit.class)
                .setParameter("userId", userId)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate).getResultList();
    }
}
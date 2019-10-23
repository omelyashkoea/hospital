package ru.homework.hospital.repository.datajpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.homework.hospital.model.Visit;
import ru.homework.hospital.repository.VisitRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DataJpaVisitRepository implements VisitRepository {

    @Autowired
    private CrudVisitRepository crudVisitRepository;

    @Autowired
    private CrudUserRepository crudUserRepository;

    @Override
    @Transactional
    public Visit save(Visit visit, int userId) {
        if (!visit.isNew() && get(visit.getId(), userId) == null) {
            return null;
        }
        visit.setUser(crudUserRepository.getOne(userId));
        return crudVisitRepository.save(visit);
    }

    @Override
    public boolean delete(int id, int userId) {
        return crudVisitRepository.delete(id, userId) != 0;
    }

    @Override
    public Visit get(int id, int userId) {
        return crudVisitRepository.findById(id).filter(visit -> visit.getUser().getId() == userId).orElse(null);
    }

    @Override
    public List<Visit> getAll(int userId) {
        return crudVisitRepository.getAll(userId);
    }

    @Override
    public List<Visit> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return crudVisitRepository.getBetween(startDate, endDate, userId);
    }

    @Override
    public Visit getWithUser(int id, int userId) {
        return crudVisitRepository.getWithUser(id, userId);
    }
}

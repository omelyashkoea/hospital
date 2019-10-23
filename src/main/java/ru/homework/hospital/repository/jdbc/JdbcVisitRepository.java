package ru.homework.hospital.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.homework.hospital.model.Visit;
import ru.homework.hospital.repository.VisitRepository;
import ru.homework.hospital.util.ValidationUtil;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JdbcVisitRepository implements VisitRepository {

    private static final RowMapper<Visit> ROW_MAPPER = BeanPropertyRowMapper.newInstance(Visit.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertVisit;

    @Autowired
    public JdbcVisitRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertVisit = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("visits")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public Visit save(Visit visit, int userId) {
        ValidationUtil.validate(visit);

        MapSqlParameterSource map = new MapSqlParameterSource()
                .addValue("id", visit.getId())
                .addValue("description", visit.getDescription())
                .addValue("expert", visit.getExpert())
                .addValue("date_time", visit.getDateTime())
                .addValue("user_id", userId);

        if (visit.isNew()) {
            Number newId = insertVisit.executeAndReturnKey(map);
            visit.setId(newId.intValue());
        } else {
            if (namedParameterJdbcTemplate.update("" +
                            "UPDATE visits " +
                            "   SET description=:description, expert=:expert, date_time=:date_time " +
                            " WHERE id=:id AND user_id=:user_id"
                    , map) == 0) {
                return null;
            }
        }
        return visit;
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        return jdbcTemplate.update("DELETE FROM visits WHERE id=? AND user_id=?", id, userId) != 0;
    }

    @Override
    public Visit get(int id, int userId) {
        List<Visit> visits = jdbcTemplate.query(
                "SELECT * FROM visits WHERE id = ? AND user_id = ?", ROW_MAPPER, id, userId);
        return DataAccessUtils.singleResult(visits);
    }

    @Override
    public List<Visit> getAll(int userId) {
        return jdbcTemplate.query(
                "SELECT * FROM visits WHERE user_id=? ORDER BY date_time DESC", ROW_MAPPER, userId);
    }

    @Override
    public List<Visit> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return jdbcTemplate.query(
                "SELECT * FROM visits WHERE user_id=?  AND date_time BETWEEN  ? AND ? ORDER BY date_time DESC",
                ROW_MAPPER, userId, startDate, endDate);
    }
}

package com.example.repositories;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.models.PromoCode;
import com.example.models.PromoType;
import com.example.models.PromoUsageType;

@Repository
public class PromoCodeRepositoryJdbcTemplateImpl implements PromoCodeRepository {

    private static final String SQL_SELECT_ALL = "SELECT id, code, type, \"value\", usage_type, active, expires_at from promocode order by id;";
    
    private static final String SQL_SELECT_BY_ID = "SELECT id, code, type, \"value\", usage_type, active, expires_at from promocode where id = ?";

    private static final String SQL_INSERT = "insert into " +
            "promocode(code, type, \"value\", usage_type, active, expires_at) values (?, ?, ?, ?, ?, ?)";

    private static final String SQL_UPDATE = "update promocode set code = ?, " +
            "type = ?, \"value\" = ?, usage_type = ?, active = ?, expires_at = ? where id = ?";

    private static final String SQL_SELECT_BY_CODE =
        "SELECT id, code, type, \"value\", usage_type, active, expires_at FROM promocode WHERE code = ?";
    private static final String SQL_SELECT_ACTIVE_AFTER_DATE =
        "SELECT id, code, type, \"value\", usage_type, active, expires_at FROM promocode WHERE active = true AND expires_at > ? ORDER BY id";
    private static final String SQL_DELETE_BY_ID =
        "DELETE FROM promocode WHERE id = ?";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PromoCodeRepositoryJdbcTemplateImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public PromoCodeRepositoryJdbcTemplateImpl(DataSource dataSource) {
        this(new JdbcTemplate(dataSource));
    }

    private final RowMapper<PromoCode> promoRowMapper = (row, i) ->
            PromoCode.builder()
                    .id(row.getLong("id"))
                    .code(row.getString("code"))
                    .type(PromoType.valueOf(row.getString("type")))
                    .value(row.getDouble("value"))
                    .usageType(PromoUsageType.valueOf(row.getString("usage_type")))
                    .active(row.getBoolean("active"))
                    .expiresAt(Optional.ofNullable(row.getTimestamp("expires_at"))
                            .map(Timestamp::toLocalDateTime)
                            .orElse(null))
                    .build();

    @Override
    public List<PromoCode> findAll() {
        return jdbcTemplate.query(SQL_SELECT_ALL, promoRowMapper);
    }

    @Override
    public Optional<PromoCode> findById(Long id) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(SQL_SELECT_BY_ID, promoRowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public PromoCode save(PromoCode promoCode) {
        if (promoCode.getId() == null) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement statement = connection.prepareStatement(SQL_INSERT, new String[]{"id"});
                statement.setString(1, promoCode.getCode());
                statement.setString(2, promoCode.getType().name());
                statement.setDouble(3, promoCode.getValue());
                statement.setString(4, promoCode.getUsageType().name());
                statement.setBoolean(5, promoCode.getActive());
                if (promoCode.getExpiresAt() == null) {
                    statement.setTimestamp(6, null);
                } else {
                    statement.setTimestamp(6, Timestamp.valueOf(promoCode.getExpiresAt()));
                }
                return statement;
            }, keyHolder);
            promoCode.setId(keyHolder.getKey().longValue());
        } else {
            jdbcTemplate.update(SQL_UPDATE, promoCode.getCode(),
                    promoCode.getType().name(), promoCode.getValue(), promoCode.getUsageType().name(),
                    promoCode.getActive(),
                    promoCode.getExpiresAt() == null ? null : Timestamp.valueOf(promoCode.getExpiresAt()),
                    promoCode.getId());
        }
        return promoCode;
    }

    @Override
    public void delete(PromoCode promoCode) {
        if (promoCode != null && promoCode.getId() != null) {
            deleteById(promoCode.getId());
        }
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(SQL_DELETE_BY_ID, id);
    }

    @Override
    public Optional<PromoCode> findByCode(String code) {
        try {
            return Optional.ofNullable(
                jdbcTemplate.queryForObject(SQL_SELECT_BY_CODE, promoRowMapper, code)
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<PromoCode> findByActiveTrueAndExpiresAtAfter(LocalDateTime dateTime) {
        return jdbcTemplate.query(SQL_SELECT_ACTIVE_AFTER_DATE, promoRowMapper, Timestamp.valueOf(dateTime));
    }
}

package com.example.repositories;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.example.models.PromoCode;
import com.example.models.PromoType;
import com.example.models.PromoUsageType;

public class PromoCodeRepositoryJdbcTemplateImpl implements PromoCodeRepository{

    private static final String SQL_SELECT_ALL = "SELECT id, code, type, value, usage_type, active, expires_at from promocode order by id;";
    
    private static final String SQL_SELECT_BY_ID = "SELECT id, code, type, value, usage_type, active, expires_at from promocode where id = ?";

    private static final String SQL_INSERT = "insert into " +
            "promocode(code, type, value, usage_type, active, expires_at) values (?, ?, ?, ?, ?, ?)";

    private static final String SQL_UPDATE = "update promocode set code = ?, " +
            "type = ?, value = ?, usage_type = ?, active = ?, expires_at = ? where id = ?";

    private static final String SQL_SELECT_BY_CODE =
        "SELECT id, code, type, value, usage_type, active, expires_at FROM promocode WHERE code = ?";

    private static final String SQL_DELETE_BY_ID =
        "DELETE FROM promocode WHERE id = ?";

    private final JdbcTemplate jdbcTemplate;

    public PromoCodeRepositoryJdbcTemplateImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private RowMapper<PromoCode> promoRowMapper = (row, i) ->
            PromoCode.builder()
                    .id(row.getLong("id"))
                    .code(row.getString("code"))
                    .type(PromoType.valueOf(row.getString("type")))
                    .value(row.getDouble("value"))
                    .usageType(PromoUsageType.valueOf(row.getString("usage_type")))
                    .active(row.getBoolean("active"))
                    .expiresAt(row.getTimestamp("expires_at").toLocalDateTime())
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
    public void save(PromoCode promoCode) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                connection -> {
                    PreparedStatement statement =
                            connection.prepareStatement(SQL_INSERT, new String[]{"id"});

                    statement.setString(1, promoCode.getCode());
                    statement.setString(2, promoCode.getType().name());
                    statement.setDouble(3, promoCode.getValue());
                    statement.setString(4, promoCode.getUsageType().name());
                    statement.setBoolean(5, promoCode.getActive());
                    statement.setTimestamp(6, Timestamp.valueOf(promoCode.getExpiresAt()));

                    return statement;
                },
                keyHolder
        );

        promoCode.setId(keyHolder.getKey().longValue());
    }

    @Override
    public void update(PromoCode promoCode) {
        jdbcTemplate.update(SQL_UPDATE, promoCode.getCode(), 
            promoCode.getType().name(), promoCode.getValue(), promoCode.getUsageType().name(), 
            promoCode.getActive(), Timestamp.valueOf(promoCode.getExpiresAt()), promoCode.getId());
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


}

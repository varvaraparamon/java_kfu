package com.example.repositories;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.example.models.Cart;

public class CartRepositoryJdbcTemplateImpl implements CartRepository {

    private static final String SQL_SELECT_ALL =
            "SELECT id, user_id, applied_promo_code_id FROM cart ORDER BY id";
    private static final String SQL_SELECT_BY_ID =
            "SELECT id, user_id, applied_promo_code_id FROM cart WHERE id = ?";
    private static final String SQL_SELECT_BY_USER_ID =
            "SELECT id, user_id, applied_promo_code_id FROM cart WHERE user_id = ?";
    private static final String SQL_INSERT =
            "INSERT INTO cart(user_id, applied_promo_code_id) VALUES (?, ?)";
    private static final String SQL_UPDATE =
            "UPDATE cart SET user_id = ?, applied_promo_code_id = ? WHERE id = ?";
    private static final String SQL_DELETE_BY_ID =
            "DELETE FROM cart WHERE id = ?";

    private final JdbcTemplate jdbcTemplate;

    public CartRepositoryJdbcTemplateImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public CartRepositoryJdbcTemplateImpl(DataSource dataSource) {
        this(new JdbcTemplate(dataSource));
    }

    private final RowMapper<Cart> cartRowMapper = (row, rowNum) -> {
        long promoId = row.getLong("applied_promo_code_id");
        boolean promoWasNull = row.wasNull();
        return Cart.builder()
                .id(row.getLong("id"))
                .userId(row.getLong("user_id"))
                .appliedPromoCodeId(promoWasNull ? null : promoId)
                .build();
    };

    @Override
    public List<Cart> findAll() {
        return jdbcTemplate.query(SQL_SELECT_ALL, cartRowMapper);
    }

    @Override
    public Optional<Cart> findById(Long id) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(SQL_SELECT_BY_ID, cartRowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Cart> findByUserId(Long userId) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(SQL_SELECT_BY_USER_ID, cartRowMapper, userId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Cart save(Cart cart) {
        if (cart.getId() == null) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_INSERT, new String[]{"id"});
                ps.setLong(1, cart.getUserId());
                if (cart.getAppliedPromoCodeId() == null) {
                    ps.setObject(2, null);
                } else {
                    ps.setLong(2, cart.getAppliedPromoCodeId());
                }
                return ps;
            }, keyHolder);
            cart.setId(keyHolder.getKey().longValue());
        } else {
            jdbcTemplate.update(SQL_UPDATE, cart.getUserId(), cart.getAppliedPromoCodeId(), cart.getId());
        }
        return cart;
    }

    @Override
    public void delete(Cart cart) {
        if (cart != null && cart.getId() != null) {
            deleteById(cart.getId());
        }
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(SQL_DELETE_BY_ID, id);
    }
}

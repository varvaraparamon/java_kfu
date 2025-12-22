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

public class CartRepositoryJdbcTemplateImpl implements CartRepository{

    private static final String SQL_SELECT_ALL = "SELECT id, user_id, applied_promo_code_id from cart order by id;";
    
    private static final String SQL_SELECT_BY_ID = "SELECT id, user_id, applied_promo_code_id from cart where id = ?";

    private static final String SQL_INSERT = "insert into " +
            "cart(user_id) values (?)";

    private static final String SQL_UPDATE = "update cart set applied_promo_code_id = ? " +
            "where id = ?";

    private final JdbcTemplate jdbcTemplate;

    public CartRepositoryJdbcTemplateImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private RowMapper<Cart> cartRowMapper = (row, rowNumber) -> Cart.builder()
            .id(row.getLong("id"))
            .userId(row.getLong("user_id"))
            .appliedPromoCodeId(row.getLong("applied_promo_code_id"))
            .build();

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
    public void save(Cart cart) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                connection -> {
                    PreparedStatement statement = connection.prepareStatement(SQL_INSERT, new String[]{"id"});

                statement.setLong(1, cart.getUserId());
                return statement;
                }, keyHolder);

        cart.setId(keyHolder.getKey().longValue());
    }

    @Override
    public void update(Cart cart) {
        jdbcTemplate.update(SQL_UPDATE, cart.getAppliedPromoCodeId(), cart.getId());
    }

    @Override
    public void delete(Cart account) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public void deleteById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }

}

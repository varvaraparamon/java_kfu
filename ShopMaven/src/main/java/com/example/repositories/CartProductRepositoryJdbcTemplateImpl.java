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

import com.example.models.CartProduct;

public class CartProductRepositoryJdbcTemplateImpl implements CartProductRepository{

    private static final String SQL_SELECT_ALL = "SELECT id, cart_id, product_id, total_count from cart_product order by id;";
    
    private static final String SQL_SELECT_BY_ID = "SELECT id, cart_id, product_id, total_count from cart_product user where id = ?";

    private static final String SQL_INSERT = "insert into " +
            "cart_product(cart_id, product_id, total_count) values (?, ?, ?)";

    private static final String SQL_UPDATE = "update cart_product set cart_id = ?, " +
            "product_id = ?, total_count = ? where id = ?";
    
    private static final String SQL_SELECT_BY_CART_ID = "SELECT id, cart_id, product_id, total_count FROM cart_product WHERE cart_id = ? ORDER BY id";

    private final JdbcTemplate jdbcTemplate;

    public CartProductRepositoryJdbcTemplateImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private RowMapper<CartProduct> cartProductRowMapper = (row, rowNumber) -> CartProduct.builder()
            .id(row.getLong("id"))
            .cartId(row.getLong("cart_id"))
            .productId(row.getLong("product_id"))
            .count(row.getInt("total_count"))
            .build();

    @Override
    public List<CartProduct> findAll() {
        return jdbcTemplate.query(SQL_SELECT_ALL, cartProductRowMapper);
    }

    @Override
    public Optional<CartProduct> findById(Long id) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(SQL_SELECT_BY_ID, cartProductRowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void save(CartProduct cartProduct) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                connection -> {
                    PreparedStatement statement = connection.prepareStatement(SQL_INSERT, new String[]{"id"});

                statement.setLong(1, cartProduct.getCartId());
                statement.setLong(2, cartProduct.getProductId());
                statement.setLong(3, cartProduct.getCount());
                return statement;
                }, keyHolder);

        cartProduct.setId(keyHolder.getKey().longValue());
    }

    @Override
    public void update(CartProduct cartProduct) {
        jdbcTemplate.update(SQL_UPDATE, cartProduct.getCartId(), 
            cartProduct.getProductId(), cartProduct.getCount(), cartProduct.getId());
    }

    @Override
    public void delete(CartProduct account) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public void deleteById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }


    @Override
    public List<CartProduct> findByCartId(Long cartId) {
        return jdbcTemplate.query(SQL_SELECT_BY_CART_ID, cartProductRowMapper, cartId);
    }
    

}

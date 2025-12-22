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

import com.example.models.Product;

public class ProductRepositoryJdbcTemplateImpl implements ProductRepository{

    private static final String SQL_SELECT_ALL = "SELECT id, name, description, price from products order by id;";
    
    private static final String SQL_SELECT_BY_ID = "SELECT id, name, description, price from product where id = ?";

    private static final String SQL_INSERT = "insert into " +
            "product(name, description, price) values (?, ?, ?)";

    private static final String SQL_UPDATE = "update product set name = ?, " +
            "description = ?, price = ? where id = ?";

    private final JdbcTemplate jdbcTemplate;

    public ProductRepositoryJdbcTemplateImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private RowMapper<Product> productRowMapper = (row, rowNumber) -> Product.builder()
            .id(row.getLong("id"))
            .name(row.getString("name"))
            .description(row.getString("description"))
            .price(row.getDouble("price"))
            .build();


    @Override
    public List<Product> findAll() {
        return jdbcTemplate.query(SQL_SELECT_ALL, productRowMapper);
    }

    @Override
    public Optional<Product> findById(Long id) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(SQL_SELECT_BY_ID, productRowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void save(Product product) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                connection -> {
                    PreparedStatement statement = connection.prepareStatement(SQL_INSERT, new String[]{"id"});

                statement.setString(1, product.getName());
                statement.setString(2, product.getDescription());
                statement.setDouble(3, product.getPrice());
                return statement;
                }, keyHolder);

        product.setId(keyHolder.getKey().longValue());
    }

    @Override
    public void update(Product product) {
        jdbcTemplate.update(SQL_UPDATE, product.getName(), 
            product.getDescription(), product.getPrice(), product.getId());
    }

    @Override
    public void delete(Product account) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public void deleteById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }

}

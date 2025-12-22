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

import com.example.models.User;

public class UserRepositoryJdbcTemplateImpl implements UserRepository{

    private static final String SQL_SELECT_ALL = "SELECT id, name, surname, phone, email, age from \"user\" order by id;";
    
    private static final String SQL_SELECT_BY_ID = "SELECT id, name, surname, phone, email, age from \"user\" where id = ?";

    private static final String SQL_INSERT = "insert into " +
            "\"user\"(name, surname, phone, email, age) values (?, ?, ?, ?, ?)";

    private static final String SQL_UPDATE = "update \"user\" set name = ?, " +
            "surname = ?, phone = ?, email = ?, age = ? where id = ?";

    private final JdbcTemplate jdbcTemplate;

    public UserRepositoryJdbcTemplateImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private RowMapper<User> userRowMapper = (row, rowNumber) -> User.builder()
            .id(row.getLong("id"))
            .name(row.getString("name"))
            .surname(row.getString("surname"))
            .phone(row.getString("phone"))
            .email(row.getString("email"))
            .age(row.getInt("age"))
            .build();


    @Override
    public List<User> findAll() {
        return jdbcTemplate.query(SQL_SELECT_ALL, userRowMapper);
    }

    @Override
    public Optional<User> findById(Long id) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(SQL_SELECT_BY_ID, userRowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

    }

    @Override
    public void save(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                connection -> {
                    PreparedStatement statement = connection.prepareStatement(SQL_INSERT, new String[]{"id"});

                statement.setString(1, user.getName());
                statement.setString(2, user.getSurname());
                statement.setString(3, user.getPhone());
                statement.setString(4, user.getEmail());
                statement.setInt(5, user.getAge());
                return statement;
                }, keyHolder);

        user.setId(keyHolder.getKey().longValue());

    }

    @Override
    public void update(User user) {
        jdbcTemplate.update(SQL_UPDATE, user.getName(), 
            user.getSurname(), user.getPhone(), 
            user.getEmail(), user.getAge(), user.getId());
    }

    @Override
    public void delete(User account) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public void deleteById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }

}

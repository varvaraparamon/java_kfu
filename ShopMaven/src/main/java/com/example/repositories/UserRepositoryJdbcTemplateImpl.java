package com.example.repositories;

import java.sql.PreparedStatement;
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

import com.example.models.User;

@Repository
public class UserRepositoryJdbcTemplateImpl implements UserRepository {

    private static final String SQL_SELECT_ALL = "SELECT id, name, surname, phone, email, age from \"user\" order by id;";
    private static final String SQL_SELECT_BY_ID = "SELECT id, name, surname, phone, email, age from \"user\" where id = ?";
    private static final String SQL_SELECT_BY_EMAIL = "SELECT id, name, surname, phone, email, age from \"user\" where email = ?";
    private static final String SQL_INSERT = "insert into \"user\"(name, surname, phone, email, age) values (?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "update \"user\" set name = ?, surname = ?, phone = ?, email = ?, age = ? where id = ?";
    private static final String SQL_DELETE_BY_ID = "delete from \"user\" where id = ?";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepositoryJdbcTemplateImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public UserRepositoryJdbcTemplateImpl(DataSource dataSource) {
        this(new JdbcTemplate(dataSource));
    }

    private final RowMapper<User> userRowMapper = (row, rowNumber) -> User.builder()
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
    public Optional<User> findByEmail(String email) {
        try {
            return Optional.of(jdbcTemplate.queryForObject(SQL_SELECT_BY_EMAIL, userRowMapper, email));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement statement = connection.prepareStatement(SQL_INSERT, new String[]{"id"});
                statement.setString(1, user.getName());
                statement.setString(2, user.getSurname());
                statement.setString(3, user.getPhone());
                statement.setString(4, user.getEmail());
                statement.setInt(5, user.getAge());
                return statement;
            }, keyHolder);
            user.setId(keyHolder.getKey().longValue());
        } else {
            jdbcTemplate.update(SQL_UPDATE, user.getName(), user.getSurname(), user.getPhone(),
                    user.getEmail(), user.getAge(), user.getId());
        }
        return user;
    }

    @Override
    public void delete(User account) {
        if (account != null && account.getId() != null) {
            deleteById(account.getId());
        }
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(SQL_DELETE_BY_ID, id);
    }
}

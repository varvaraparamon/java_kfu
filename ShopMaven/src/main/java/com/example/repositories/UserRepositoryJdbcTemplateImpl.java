package com.example.repositories;

import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

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

    // private RowMapper<User> userRowMapper = (row, rowNumber) -> new User.builder()
    //     .id(row.getLong("id")),
    //     row.getString("name"),
    //     row.getString("surname"),
    //     row.getString("phone"),
    //     row.getString("email"),
    //     row.getInt("age");


    @Override
    public List<User> findAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public Optional<User> findById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    @Override
    public void save(User account) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public void update(User account) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
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

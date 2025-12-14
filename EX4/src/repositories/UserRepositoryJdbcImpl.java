package EX4.src.repositories;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import EX4.src.models.User;

public class UserRepositoryJdbcImpl implements UserRepository {

    private static final String SQL_SELECT_ALL = "SELECT id, name, surname, phone, email, age from \"user\" order by id;";
    
    private static final String SQL_SELECT_BY_ID = "SELECT id, name, surname, phone, email, age from \"user\" where id = ?";

    private static final String SQL_INSERT = "insert into " +
            "\"user\"(name, surname, phone, email, age) values (?, ?, ?, ?, ?)";

    private static final String SQL_UPDATE = "update \"user\" set name = ?, " +
            "surname = ?, phone = ?, email = ?, age = ? where id = ?";

    private Connection connection;

    public UserRepositoryJdbcImpl(Connection connection) {
        this.connection = connection;
    }

    private RowMapper<User> userRowMapper = row -> new User(
            row.getLong("id"),
            row.getString("name"),
            row.getString("surname"),
            row.getString("phone"),
            row.getString("email"),
            row.getInt("age"));


    public List<User> findAll() {
        Statement statement = null;
        ResultSet rows = null;
        try {
            List<User> users = new ArrayList<>();

            statement = connection.createStatement();
            rows = statement.executeQuery(SQL_SELECT_ALL);

            while (rows.next()) {
                User user = userRowMapper.mapRow(rows);
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            if (rows != null) {
                try {
                    rows.close();
                } catch (SQLException throwables) {
                    // ignore
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException throwables) {
                    // ignore
                }
            }
        }
    }

    @Override
    public void save(User user) {
        PreparedStatement statement = null;
        ResultSet generatedKeys = null;
        try {
            statement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, user.getName());
            statement.setString(2, user.getSurname());
            statement.setString(3, user.getPhone());
            statement.setString(4, user.getEmail());
            statement.setInt(5, user.getAge());

            int affectedRows = statement.executeUpdate();

            if (affectedRows != 1) {
                throw new SQLException("Can't insert");
            }

            generatedKeys = statement.getGeneratedKeys();

            if (generatedKeys.next()) {
                user.setId(generatedKeys.getLong("id"));
            } else {
                throw new SQLException("Can't retrieve id");
            }

        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            if (generatedKeys != null) {
                try {
                    generatedKeys.close();
                } catch (SQLException throwables) {
                    // ignore
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException throwables) {
                    // ignore
                }
            }
        }

    }

    @Override
    public User findById(Long id) {
        PreparedStatement statement = null;
        ResultSet rows = null;
        try {
            statement = connection.prepareStatement(SQL_SELECT_BY_ID);
            statement.setLong(1, id);
            rows = statement.executeQuery();

            if (rows.next()) {
                return userRowMapper.mapRow(rows);
            }
            return null;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            if (rows != null) {
                try {
                    rows.close();
                } catch (SQLException throwables) {
                    // ignore
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException throwables) {
                    // ignore
                }
            }
        }
    }

    public void update(User user) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(SQL_UPDATE);

            statement.setString(1, user.getName());
            statement.setString(2, user.getSurname());
            statement.setString(3, user.getPhone());
            statement.setString(4, user.getEmail());
            statement.setInt(5, user.getAge());
            statement.setLong(6, user.getId());

            int affectedRows = statement.executeUpdate();

            if (affectedRows != 1) {
                throw new SQLException("Can't update");
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException throwables) {
                    // ignore
                }
            }
        }
    }


}

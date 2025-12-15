package EX4.src.repositories;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


import EX4.src.models.Cart;

public class CartRepositoryJdbcImpl implements CartRepository{

    private static final String SQL_SELECT_ALL = "SELECT id, user_id, current_size from cart order by id;";
    
    private static final String SQL_SELECT_BY_ID = "SELECT id, user_id, current_size from cart where id = ?";

    private static final String SQL_INSERT = "insert into " +
            "cart(user_id) values (?)";

    private static final String SQL_UPDATE = "update cart set current_size = ? " +
            "where id = ?";

    private Connection connection;

    public CartRepositoryJdbcImpl(Connection connection) {
        this.connection = connection;
    }

    private RowMapper<Cart> cartRowMapper = row -> new Cart(
            row.getLong("id"),
            row.getLong("user_id"),
            row.getInt("current_size")
        );


    public List<Cart> findAll() {
        Statement statement = null;
        ResultSet rows = null;

        try {
            List<Cart> carts = new ArrayList<>();

            statement = connection.createStatement();
            rows = statement.executeQuery(SQL_SELECT_ALL);

            while (rows.next()) {
                Cart cart = cartRowMapper.mapRow(rows);
                carts.add(cart);
            }
            return carts;

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
    public Cart findById(Long id) {
        PreparedStatement statement = null;
        ResultSet rows = null;

        try {
            statement = connection.prepareStatement(SQL_SELECT_BY_ID);
            statement.setLong(1, id);

            rows = statement.executeQuery();

            if (rows.next()) {
                return cartRowMapper.mapRow(rows);
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

    @Override
    public void save(Cart cart) {
        PreparedStatement statement = null;
        ResultSet generatedKeys = null;

        try {
            statement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);

            statement.setLong(1, cart.getUserId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows != 1) {
                throw new SQLException("Can't insert cart");
            }

            generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                cart.setId(generatedKeys.getLong("id"));
            } else {
                throw new SQLException("Can't retrieve id for cart");
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
    public void update(Cart cart) {
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(SQL_UPDATE);

            statement.setInt(1, cart.getCurrentSize());
            statement.setLong(2, cart.getId());

            int affectedRows = statement.executeUpdate();

            if (affectedRows != 1) {
                throw new SQLException("Can't update cart");
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



package EX4.src.repositories;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


import EX4.src.models.CartProduct;


public class CartProductRepositoryJdbcImpl implements CartProductRepository{

    private static final String SQL_SELECT_ALL = "SELECT id, cart_id, product_id, total_count from cart_product order by id;";
    
    private static final String SQL_SELECT_BY_ID = "SELECT id, cart_id, product_id, total_count from cart_product user where id = ?";

    private static final String SQL_INSERT = "insert into " +
            "cart_product(cart_id, product_id, total_count) values (?, ?, ?)";

    private static final String SQL_UPDATE = "update cart_product set cart_id = ?, " +
            "product_id = ?, total_count = ? where id = ?";

    private Connection connection;

    public CartProductRepositoryJdbcImpl(Connection connection) {
        this.connection = connection;
    }

    private RowMapper<CartProduct> cartProductRowMapper = row -> new CartProduct(
            row.getLong("id"),
            row.getLong("cart_id"),
            row.getLong("product_id"),
            row.getInt("total_count"));


    public List<CartProduct> findAll() {
        Statement statement = null;
        ResultSet rows = null;
        try {
            List<CartProduct> cartProducts = new ArrayList<>();

            statement = connection.createStatement();
            rows = statement.executeQuery(SQL_SELECT_ALL);

            while (rows.next()) {
                CartProduct cartProduct = cartProductRowMapper.mapRow(rows);
                cartProducts.add(cartProduct);
            }
            return cartProducts;
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
    public void save(CartProduct cartProduct) {
        PreparedStatement statement = null;
        ResultSet generatedKeys = null;
        try {
            statement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);

            statement.setLong(1, cartProduct.getCartId());
            statement.setLong(2, cartProduct.getProductId());
            statement.setInt(3, cartProduct.getCount());

            int affectedRows = statement.executeUpdate();

            if (affectedRows != 1) {
                throw new SQLException("Can't insert");
            }

            generatedKeys = statement.getGeneratedKeys();

            if (generatedKeys.next()) {
                cartProduct.setId(generatedKeys.getLong("id"));
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
    public CartProduct findById(Long id) {
        PreparedStatement statement = null;
        ResultSet rows = null;
        try {
            statement = connection.prepareStatement(SQL_SELECT_BY_ID);
            statement.setLong(1, id);
            rows = statement.executeQuery();

            if (rows.next()) {
                return cartProductRowMapper.mapRow(rows);
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


    public void update(CartProduct cartProduct) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(SQL_UPDATE);

            statement.setLong(1, cartProduct.getCartId());
            statement.setLong(2, cartProduct.getProductId());
            statement.setInt(3, cartProduct.getCount());
            statement.setLong(4, cartProduct.getId());

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

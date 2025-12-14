package EX4.src.repositories;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import EX4.src.models.Product;

public class ProductRepositoryJdbcImpl implements ProductRepository{

    private static final String SQL_SELECT_ALL = "SELECT id, name, description, price from products order by id;";
    
    private static final String SQL_SELECT_BY_ID = "SELECT id, name, description, price from product where id = ?";

    private static final String SQL_INSERT = "insert into " +
            "product(name, description, price) values (?, ?, ?)";

    private static final String SQL_UPDATE = "update product set name = ?, " +
            "description = ?, price = ? where id = ?";

    private Connection connection;

    public ProductRepositoryJdbcImpl(Connection connection) {
        this.connection = connection;
    }

    private RowMapper<Product> productRowMapper = row -> new Product(
            row.getLong("id"),
            row.getString("name"),
            row.getString("description"),
            row.getDouble("price"));


    public List<Product> findAll() {
        Statement statement = null;
        ResultSet rows = null;
        try {
            List<Product> products = new ArrayList<>();

            statement = connection.createStatement();
            rows = statement.executeQuery(SQL_SELECT_ALL);

            while (rows.next()) {
                Product product = productRowMapper.mapRow(rows);
                products.add(product);
            }
            return products;
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
    public void save(Product product) {
        PreparedStatement statement = null;
        ResultSet generatedKeys = null;
        try {
            statement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, product.getName());
            statement.setString(2, product.getDescription());
            statement.setDouble(3, product.getPrice());

            int affectedRows = statement.executeUpdate();

            if (affectedRows != 1) {
                throw new SQLException("Can't insert");
            }

            generatedKeys = statement.getGeneratedKeys();

            if (generatedKeys.next()) {
                product.setId(generatedKeys.getLong("id"));
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
    public Product findById(Long id) {
        PreparedStatement statement = null;
        ResultSet rows = null;
        try {
            statement = connection.prepareStatement(SQL_SELECT_BY_ID);
            statement.setLong(1, id);
            rows = statement.executeQuery();

            if (rows.next()) {
                return productRowMapper.mapRow(rows);
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






    public void update(Product product) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(SQL_UPDATE);

            statement.setString(1, product.getName());
            statement.setString(2, product.getDescription());
            statement.setDouble(3, product.getPrice());
            statement.setLong(4, product.getId());

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


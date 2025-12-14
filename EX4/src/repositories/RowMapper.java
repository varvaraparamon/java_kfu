package EX4.src.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 24.04.2021
 * 28. DB
 *
 * @author Sidikov Marsel (First Software Engineering Platform)
 * @version v1.0
 */
public interface RowMapper<T> {
    T mapRow(ResultSet row) throws SQLException;
}

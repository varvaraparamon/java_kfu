package com.example;

import com.example.framework.DataModelFramework;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/framework",
                "postgres",
                "password"
        );
        DataModelFramework framework = new DataModelFramework(conn);

        framework.selectAll(Item.class);
        framework.selectAll(CartItem.class);
    }
}

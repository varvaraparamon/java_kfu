package com.example.framework;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

public class DataModelFramework {
    private final Connection connection;

    public DataModelFramework(Connection connection) {
        this.connection = connection;
    }

    public <T extends DataModel> void selectAll(Class<T> modelClass) {
        String tableName = resolveTableName(modelClass);
        String sql = "SELECT * FROM " + tableName;

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                Constructor<T> constructor = modelClass.getConstructor();
                T model = constructor.newInstance();
                processFields(model, rs);
                System.out.println(model);
            }

        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    private String resolveTableName(Class<?> modelClass) {
        if (modelClass.isAnnotationPresent(Table.class)) {
            return modelClass.getAnnotation(Table.class).value();
        }
        return toSnakeCase(modelClass.getSimpleName());
    }

    private String toSnakeCase(String name) {
        return name
                .replaceAll("([a-z])([A-Z])", "$1_$2")
                .toLowerCase();
    }

    private <T> void processFields(T model, ResultSet rs) throws Exception {
        Field[] fields = model.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(DisabledColumn.class)) {
                continue;
            }

            field.setAccessible(true);

            String columnName = field.isAnnotationPresent(Column.class)
                    ? field.getAnnotation(Column.class).value()
                    : toSnakeCase(field.getName());

            Object value = rs.getObject(columnName);
            field.set(model, convertValue(value, field.getType()));
        }
    }

    private Object convertValue(Object value, Class<?> targetType) {
        if (value == null) return null;

        if (value.getClass() == targetType) return value;

        if (value instanceof Number number) {
            if (targetType == Long.class    || targetType == long.class)   return number.longValue();
            if (targetType == Integer.class || targetType == int.class)    return number.intValue();
            if (targetType == Double.class  || targetType == double.class) return number.doubleValue();
            if (targetType == Float.class   || targetType == float.class)  return number.floatValue();
        }

        if (targetType == String.class) return value.toString();

        if (targetType == LocalDate.class && value instanceof java.sql.Date date) {
            return date.toLocalDate();
        }

        return value;
    }
}

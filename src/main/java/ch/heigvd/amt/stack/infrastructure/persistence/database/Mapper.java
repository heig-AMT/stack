package ch.heigvd.amt.stack.infrastructure.persistence.database;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface Mapper<T> {
    T apply(ResultSet resultSet) throws SQLException;
}

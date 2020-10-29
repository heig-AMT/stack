package ch.heigvd.amt.stack.infrastructure.persistence.database.dsl;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A functional interface that defines a mapping between a {@link ResultSet} entry to a generic type.
 *
 * @param <T> the type we want to transform to.
 */
@FunctionalInterface
public interface Mapper<T> {
    T apply(ResultSet resultSet) throws SQLException;
}

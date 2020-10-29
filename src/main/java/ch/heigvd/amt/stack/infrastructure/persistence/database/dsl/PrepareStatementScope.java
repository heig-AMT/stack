package ch.heigvd.amt.stack.infrastructure.persistence.database.dsl;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * A functional interface that's useful to prepare a statement with additional parameters.
 */
@FunctionalInterface
public interface PrepareStatementScope {

    /**
     * An empty {@link PrepareStatementScope}, for statements that do not need additional configuration.
     */
    static PrepareStatementScope none() {
        return statement -> {
        };
    }

    /**
     * Prepares a statement with additional parameters.
     *
     * @param statement the statement to prepare.
     * @throws SQLException hanlded by the caller.
     */
    void prepare(PreparedStatement statement) throws SQLException;
}

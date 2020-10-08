package ch.heigvd.amt.stack.ui.web.views;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

/**
 * A class that's meant to investigate how things are going on in Heroku. This will be removed soon.
 *
 * TODO : REMOVE THIS CLASS ONCE INVESTIGATIONS ARE DONE.
 */
@WebFilter("/*")
public class ProvideDebugFilter implements Filter {

    @Resource(name = "database")
    private DataSource database;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println(this.getClass().getSimpleName() + " " + System.getenv("DATABASE_URL"));
        System.out.println(this.getClass().getSimpleName() + " " + System.getenv("JDBC_DATABASE_URL"));
        System.out.println(this.getClass().getSimpleName() + " " + System.getenv("JDBC_DATABASE_USERNAME"));
        System.out.println(this.getClass().getSimpleName() + " " + System.getenv("JDBC_DATABASE_PASSWORD"));

        testJdbc();

        chain.doFilter(request, response);
    }

    private void testJdbc() {
        try {
            var sql =
                    "CREATE TABLE IF NOT EXISTS Hello(id INT NOT NULL); " +
                            "INSERT INTO Hello VALUES (1234); " +
                            "INSERT INTO Hello VALUES (1234);";

            try (var connection = database.getConnection()) {
                try (var statement = connection.prepareStatement(sql)) {
                    statement.execute();
                }
                try (var statement = connection.prepareStatement("SELECT * FROM Hello;")) {
                    var results = statement.executeQuery();
                    while (results.next()) {
                        System.out.println("Hello : " + results.getInt("id"));
                    }
                }
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        } catch (Throwable any) {
            any.printStackTrace();
        }
    }

    @Override
    public void destroy() {

    }
}

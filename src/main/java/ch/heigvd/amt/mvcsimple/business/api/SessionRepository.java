package ch.heigvd.amt.mvcsimple.business.api;

import javax.ejb.Local;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Local
public interface SessionRepository {

    void link(HttpSession session, String username);

    void unlink(HttpSession session);

    Optional<String> username(HttpSession session);
}
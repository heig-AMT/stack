package ch.heigvd.amt.mvcsimple.business.impl;

import ch.heigvd.amt.mvcsimple.business.api.SessionRepository;

import javax.ejb.Singleton;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Singleton
public class InMemorySessionRepository implements SessionRepository {

    private final Map<String, String> store = new HashMap<>();

    @Override
    public void link(HttpSession session, String username) {
        store.put(session.getId(), username);
    }

    @Override
    public void unlink(HttpSession session) {
        store.remove(session.getId());
    }

    @Override
    public Optional<String> username(HttpSession session) {
        return Optional.ofNullable(store.get(session.getId()));
    }
}

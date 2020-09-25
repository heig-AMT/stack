package ch.heigvd.amt.mvcsimple.business.impl;

import ch.heigvd.amt.mvcsimple.business.api.CredentialRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class InMemoryCredentialRepository implements CredentialRepository {

    private Map<String, String> credentials = new HashMap<>();

    @Override
    public boolean registered(String username) {
        return credentials.containsKey(username);
    }

    @Override
    public void set(String username, String password) {
        credentials.put(username, password);
    }

    @Override
    public void clear(String username) {
        credentials.remove(username);
    }

    @Override
    public boolean match(String username, String password) {
        return Objects.equals(password, credentials.get(username));
    }
}

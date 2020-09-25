package ch.heigvd.amt.mvcsimple;

import ch.heigvd.amt.mvcsimple.business.api.CredentialRepository;
import ch.heigvd.amt.mvcsimple.business.api.SessionRepository;
import ch.heigvd.amt.mvcsimple.business.impl.InMemoryCredentialRepository;
import ch.heigvd.amt.mvcsimple.business.impl.InMemorySessionRepository;

public class Repositories {

    private final CredentialRepository credentialRepository;
    private final SessionRepository sessionRepository;

    private static Repositories repositories;

    public static synchronized Repositories getInstance() {
        if (repositories == null) {
            repositories = new Repositories();
        }
        return repositories;
    }

    private Repositories() {
        credentialRepository = new InMemoryCredentialRepository();
        sessionRepository = new InMemorySessionRepository();
    }

    public CredentialRepository getCredentialRepository() {
        return credentialRepository;
    }

    public SessionRepository getSessionRepository() {
        return sessionRepository;
    }
}

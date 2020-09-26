package ch.heigvd.amt.stack.application;

import ch.heigvd.amt.stack.application.authentication.AuthenticationFacade;
import ch.heigvd.amt.stack.application.question.QuestionFacade;
import ch.heigvd.amt.stack.infrastructure.persistence.memory.InMemoryCredentialRepository;
import ch.heigvd.amt.stack.infrastructure.persistence.memory.InMemoryQuestionRepository;
import ch.heigvd.amt.stack.infrastructure.persistence.memory.InMemorySessionRepository;

public class ServiceRegistry {

    private static ServiceRegistry instance;

    public static ServiceRegistry getInstance() {
        if (instance == null) {
            instance = new ServiceRegistry();
        }
        return instance;
    }

    private final AuthenticationFacade authenticationFacade;
    private final QuestionFacade questionFacade;

    private ServiceRegistry() {
        var credentialRepository = new InMemoryCredentialRepository();
        var sessionRepository = new InMemorySessionRepository();
        var questionRepository = new InMemoryQuestionRepository();

        this.authenticationFacade = new AuthenticationFacade(credentialRepository, sessionRepository);
        this.questionFacade = new QuestionFacade(credentialRepository, questionRepository, sessionRepository);
    }

    public AuthenticationFacade getAuthenticationFacade() {
        return authenticationFacade;
    }

    public QuestionFacade getQuestionFacade() {
        return questionFacade;
    }
}

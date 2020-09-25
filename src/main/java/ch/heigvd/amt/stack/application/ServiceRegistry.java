package ch.heigvd.amt.stack.application;

import ch.heigvd.amt.stack.application.question.QuestionFacade;
import ch.heigvd.amt.stack.infrastructure.persistence.memory.InMemoryQuestionRepository;

public class ServiceRegistry {

    private static ServiceRegistry instance;

    public static ServiceRegistry getInstance() {
        if (instance == null) {
            instance = new ServiceRegistry();
        }
        return instance;
    }

    private final QuestionFacade questionFacade;

    private ServiceRegistry() {
        var questionRepository = new InMemoryQuestionRepository();

        this.questionFacade = new QuestionFacade(questionRepository);
    }

    public QuestionFacade getQuestionFacade() {
        return questionFacade;
    }
}

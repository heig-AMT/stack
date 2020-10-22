package ch.heigvd.amt.stack.application.answer;

import ch.heigvd.amt.stack.application.answer.command.AnswerQuestionCommand;
import ch.heigvd.amt.stack.application.answer.dto.AnswerDTO;
import ch.heigvd.amt.stack.application.answer.query.AnswerQuery;
import ch.heigvd.amt.stack.application.authentication.AuthenticationFacade;
import ch.heigvd.amt.stack.application.authentication.command.RegisterCommand;
import ch.heigvd.amt.stack.application.question.QuestionFacade;
import ch.heigvd.amt.stack.application.question.command.AskQuestionCommand;
import ch.heigvd.amt.stack.domain.authentication.AuthenticationFailedException;
import ch.heigvd.amt.stack.domain.question.QuestionId;
import ch.heigvd.amt.stack.domain.question.QuestionNotFoundException;
import ch.heigvd.amt.stack.infrastructure.persistence.memory.InMemoryAnswerRepository;
import ch.heigvd.amt.stack.infrastructure.persistence.memory.InMemoryCredentialRepository;
import ch.heigvd.amt.stack.infrastructure.persistence.memory.InMemoryQuestionRepository;
import ch.heigvd.amt.stack.infrastructure.persistence.memory.InMemorySessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AnswerFacadeIntegration {

    private AnswerFacade answerFacade;
    private AuthenticationFacade authenticationFacade;
    private QuestionFacade questionFacade;

    @BeforeEach
    public void prepare() {
        var credentials = new InMemoryCredentialRepository();
        var answers = new InMemoryAnswerRepository();
        var questions = new InMemoryQuestionRepository();
        var sessions = new InMemorySessionRepository();

        this.answerFacade = new AnswerFacade(credentials, answers, questions, sessions);
        this.authenticationFacade = new AuthenticationFacade(credentials, sessions);
        this.questionFacade = new QuestionFacade(credentials, questions, sessions);
    }

    @Test
    public void testUnAuthenticatedUserCanNotAnswer() {
        var answer = AnswerQuestionCommand.builder()
                .question(QuestionId.create())
                .body("My wonderful answer")
                .tag("alice")
                .build();

        assertThrows(AuthenticationFailedException.class, () -> {
            this.answerFacade.answer(answer);
        });
    }

    @Test
    public void testAuthenticatedUserCanAnswerExistingQuestion() {
        var register = RegisterCommand.builder()
                .username("alice")
                .password("password")
                .tag("tag")
                .build();
        var ask = AskQuestionCommand.builder()
                .title("Title")
                .description("Description")
                .tag("tag")
                .build();

        authenticationFacade.register(register);
        var questionId = questionFacade.askQuestion(ask);

        var answer = AnswerQuestionCommand.builder()
                .question(questionId)
                .body("This is my answer.")
                .tag("tag")
                .build();

        var query = AnswerQuery.builder()
                .forQuestion(questionId)
                .build();

        assertDoesNotThrow(() -> {
            answerFacade.answer(answer);
            var result = answerFacade.getAnswers(query);
            assertEquals(1, result.getAnswers().size());
            var resultAnswer = result.getAnswers().get(0);
            assertEquals("alice", resultAnswer.getAuthor());
            assertEquals("This is my answer.", resultAnswer.getBody());
        });
    }

    @Test
    public void testAuthenticatedUserCanNotAnswerMissingQuestion() {
        var register = RegisterCommand.builder()
                .username("alice")
                .password("password")
                .tag("tag")
                .build();

        var answer = AnswerQuestionCommand.builder()
                .question(QuestionId.create())
                .body("This is a stupid question.")
                .tag("tag")
                .build();

        authenticationFacade.register(register);

        assertThrows(QuestionNotFoundException.class, () -> {
            answerFacade.answer(answer);
        });
    }
}

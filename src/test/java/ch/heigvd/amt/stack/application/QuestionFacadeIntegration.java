package ch.heigvd.amt.stack.application;

import ch.heigvd.amt.stack.application.answer.command.AnswerQuestionCommand;
import ch.heigvd.amt.stack.application.authentication.command.RegisterCommand;
import ch.heigvd.amt.stack.application.question.command.AskQuestionCommand;
import ch.heigvd.amt.stack.application.question.command.DeleteQuestionCommand;
import ch.heigvd.amt.stack.application.question.dto.QuestionStatusDTO;
import ch.heigvd.amt.stack.application.question.query.QuestionQuery;
import ch.heigvd.amt.stack.application.question.query.SingleAnswerQuery;
import ch.heigvd.amt.stack.application.question.query.SingleQuestionQuery;
import ch.heigvd.amt.stack.domain.answer.AnswerId;
import ch.heigvd.amt.stack.domain.authentication.AuthenticationFailedException;
import ch.heigvd.amt.stack.domain.question.QuestionId;
import ch.heigvd.amt.stack.infrastructure.persistence.memory.InMemoryAnswerRepository;
import ch.heigvd.amt.stack.infrastructure.persistence.memory.InMemoryCredentialRepository;
import ch.heigvd.amt.stack.infrastructure.persistence.memory.InMemoryQuestionRepository;
import ch.heigvd.amt.stack.infrastructure.persistence.memory.InMemorySessionRepository;
import ch.heigvd.amt.stack.infrastructure.persistence.mock.MockGamificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class QuestionFacadeIntegration {

    private AuthenticationFacade authenticationFacade;
    private QuestionFacade questionFacade;

    @BeforeEach
    public void prepare() {
        var answers = new InMemoryAnswerRepository();
        var credentials = new InMemoryCredentialRepository();
        var questions = new InMemoryQuestionRepository();
        var sessions = new InMemorySessionRepository();
        var games = new MockGamificationRepository();

        authenticationFacade = new AuthenticationFacade();
        authenticationFacade.credentials = credentials;
        authenticationFacade.sessions = sessions;

        questionFacade = new QuestionFacade();
        questionFacade.answerRepository = answers;
        questionFacade.credentialRepository = credentials;
        questionFacade.sessionRepository = sessions;
        questionFacade.repository = questions;
        questionFacade.gamificationRepository = games;
    }

    @Test
    public void testEmptyRepositoryDisplaysNoQuestions() {
        var query = QuestionQuery.builder().build();
        var result = questionFacade.getQuestions(query);
        assertIterableEquals(Collections.emptyList(), result.getQuestions());
    }

    @Test
    public void testUnregisteredUserCanNotAskQuestion() {
        var command = AskQuestionCommand.builder()
                .title("Hello world")
                .description("Something")
                .tag("tag")
                .build();
        assertThrows(AuthenticationFailedException.class, () -> {
            questionFacade.askQuestion(command);
        });
    }

    @Test
    public void testRegisteredUserCanAskQuestionAndSeeIt() {
        var register = RegisterCommand.builder()
                .username("alice")
                .password("password")
                .tag("tag")
                .build();
        var ask = AskQuestionCommand.builder()
                .title("What is love")
                .description("Some description")
                .tag("tag")
                .build();
        var query = QuestionQuery.builder().build();

        authenticationFacade.register(register);
        questionFacade.askQuestion(ask);

        var result = questionFacade.getQuestions(query).getQuestions();

        assertEquals(1, result.size());
        assertEquals("Some description", result.get(0).getDescription());
        assertEquals("What is love", result.get(0).getTitle());
        assertEquals("alice", result.get(0).getAuthor());
        assertEquals(QuestionStatusDTO.New, result.get(0).getStatus());
        assertNotNull(result.get(0).getCreation());
    }

    @Test
    public void testFilteringQueryHidesItems() {
        var register = RegisterCommand.builder()
                .username("alice")
                .password("password")
                .tag("tag")
                .build();
        var ask = AskQuestionCommand.builder()
                .title("What is love")
                .description("Some description")
                .tag("tag")
                .build();
        var queryExclude = QuestionQuery.builder().shouldContain("Mallory").build();

        authenticationFacade.register(register);
        questionFacade.askQuestion(ask);

        var result = questionFacade.getQuestions(queryExclude).getQuestions();

        assertTrue(result.isEmpty());
    }

    @Test
    public void testAskedQuestionsGetDifferentIds() {
        var register = RegisterCommand.builder()
                .username("alice")
                .password("password")
                .tag("tag")
                .build();
        var ask = AskQuestionCommand.builder()
                .title("What is love")
                .description("Description")
                .tag("tag")
                .build();

        authenticationFacade.register(register);
        var id1 = questionFacade.askQuestion(ask);
        var id2 = questionFacade.askQuestion(ask);

        assertNotEquals(id1, id2);
    }

    @Test
    public void testEmptyQuestionFacadeReturnsEmptyQuestionDTO() {
        var query = SingleQuestionQuery.builder()
                .id(QuestionId.create())
                .build();
        var answer = questionFacade.getQuestion(query);
        assertEquals(Optional.empty(), answer);
    }

    @Test
    public void testNonAuthenticatedUserCanNotDeleteQuestion() {
        var register = RegisterCommand.builder()
                .username("alice")
                .password("password")
                .tag("tag")
                .build();
        var ask = AskQuestionCommand.builder()
                .title("What is love")
                .description("Description")
                .tag("tag")
                .build();

        authenticationFacade.register(register);
        var id = questionFacade.askQuestion(ask);

        var delete = DeleteQuestionCommand.builder()
                .question(id)
                .tag("anotherTag")
                .build();

        assertThrows(AuthenticationFailedException.class, () -> {
           questionFacade.deleteQuestion(delete);
        });
    }

    @Test
    public void testAuthenticatedUserCanDeleteTheirOwnQuestion() {
        var register = RegisterCommand.builder()
                .username("alice")
                .password("password")
                .tag("tag")
                .build();
        var ask = AskQuestionCommand.builder()
                .title("What is love")
                .description("Description")
                .tag("tag")
                .build();

        authenticationFacade.register(register);
        var id = questionFacade.askQuestion(ask);
        var delete = DeleteQuestionCommand.builder()
                .question(id)
                .tag("tag")
                .build();

        assertDoesNotThrow(() -> {
            questionFacade.deleteQuestion(delete);
        });
    }

    @Test
    public void testAuthenticatedUserCannotDeleteSomeoneElsesQuestion() {
        final String tagAlice = "tagAlice";
        final String tagBob = "tagBob";

        var register1 = RegisterCommand.builder()
                .username("alice")
                .password("password1")
                .tag(tagAlice)
                .build();
        var register2 = RegisterCommand.builder()
                .username("bob")
                .password("password2")
                .tag(tagBob)
                .build();
        var askAlice = AskQuestionCommand.builder()
                .title("What is love")
                .description("Description")
                .tag(tagAlice)
                .build();

        authenticationFacade.register(register1);
        authenticationFacade.register(register2);
        var questionId = questionFacade.askQuestion(askAlice);

        var bobDelete = DeleteQuestionCommand.builder()
                .question(questionId)
                .tag(tagBob)
                .build();

        assertThrows(AuthenticationFailedException.class, () -> {
            questionFacade.deleteQuestion(bobDelete);
        });
    }
}

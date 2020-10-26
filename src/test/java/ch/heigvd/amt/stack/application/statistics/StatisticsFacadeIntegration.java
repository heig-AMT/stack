package ch.heigvd.amt.stack.application.statistics;

import ch.heigvd.amt.stack.application.answer.AnswerFacade;
import ch.heigvd.amt.stack.application.answer.command.AnswerQuestionCommand;
import ch.heigvd.amt.stack.application.authentication.AuthenticationFacade;
import ch.heigvd.amt.stack.application.authentication.command.RegisterCommand;
import ch.heigvd.amt.stack.application.question.QuestionFacade;
import ch.heigvd.amt.stack.application.question.command.AskQuestionCommand;
import ch.heigvd.amt.stack.application.statistics.query.UsageStatisticsQuery;
import ch.heigvd.amt.stack.infrastructure.persistence.memory.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatisticsFacadeIntegration {

    private AnswerFacade answerFacade;
    private AuthenticationFacade authenticationFacade;
    private QuestionFacade questionFacade;
    private StatisticsFacade statisticsFacade;

    @BeforeEach
    public void prepare() {
        var answers = new InMemoryAnswerRepository();
        var comments = new InMemoryCommentRepository();
        var credentials = new InMemoryCredentialRepository();
        var questions = new InMemoryQuestionRepository();
        var sessions = new InMemorySessionRepository();
        var votes = new InMemoryVoteRepository();

        this.answerFacade = new AnswerFacade(comments, credentials, answers, questions, sessions, votes);
        this.authenticationFacade = new AuthenticationFacade(credentials, sessions);
        this.questionFacade = new QuestionFacade(answers, credentials, questions, sessions);
        this.statisticsFacade = new StatisticsFacade(answers, credentials, questions);
    }

    @Test
    public void testBaseStatisticsShowNoUsage() {
        var query = new UsageStatisticsQuery();

        var stats = statisticsFacade.getUsageStatistics(query);

        assertEquals(0, stats.getAnswerCount());
        assertEquals(0, stats.getQuestionCount());
        assertEquals(0, stats.getUserCount());
    }

    @Test
    public void testTwoRegisteredUsersAreProperlyCounted() {
        var aliceRegister = RegisterCommand.builder()
                .username("alice")
                .password("password")
                .tag("tag1")
                .build();
        var bobRegister = RegisterCommand.builder()
                .username("bob")
                .password("password")
                .tag("tag2")
                .build();

        authenticationFacade.register(aliceRegister);
        authenticationFacade.register(bobRegister);

        var query = new UsageStatisticsQuery();
        var stats = statisticsFacade.getUsageStatistics(query);

        assertEquals(0, stats.getAnswerCount());
        assertEquals(0, stats.getQuestionCount());
        assertEquals(2, stats.getUserCount());
    }

    @Test
    public void testRegisteredUserCanAskThreeQuestionsThatAreProperlyCounted() {
        var aliceRegister = RegisterCommand.builder()
                .username("alice")
                .password("password")
                .tag("tag")
                .build();

        authenticationFacade.register(aliceRegister);

        var askQuestion = AskQuestionCommand.builder()
                .title("Title")
                .description("Description")
                .tag("tag")
                .build();

        questionFacade.askQuestion(askQuestion);
        questionFacade.askQuestion(askQuestion);
        questionFacade.askQuestion(askQuestion);

        var query = new UsageStatisticsQuery();
        var stats = statisticsFacade.getUsageStatistics(query);

        assertEquals(0, stats.getAnswerCount());
        assertEquals(3, stats.getQuestionCount());
        assertEquals(1, stats.getUserCount());
    }

    @Test
    public void testRegisteredUserCanAnswerQuestionsThatAreProperlyCounted() {
        var aliceRegister = RegisterCommand.builder()
                .username("alice")
                .password("password")
                .tag("tag")
                .build();
        authenticationFacade.register(aliceRegister);

        var askQuestion = AskQuestionCommand.builder()
                .title("Title")
                .description("Description")
                .tag("tag")
                .build();

        var questionId = questionFacade.askQuestion(askQuestion);

        var answerQuestion = AnswerQuestionCommand.builder()
                .question(questionId)
                .body("This is a stupid question")
                .tag("tag")
                .build();

        assertDoesNotThrow(() -> {
            answerFacade.answer(answerQuestion);
            answerFacade.answer(answerQuestion);
            answerFacade.answer(answerQuestion);
            answerFacade.answer(answerQuestion);
        });

        var query = new UsageStatisticsQuery();
        var stats = statisticsFacade.getUsageStatistics(query);

        assertEquals(4, stats.getAnswerCount());
        assertEquals(1, stats.getQuestionCount());
        assertEquals(1, stats.getUserCount());
    }
}

package ch.heigvd.amt.stack.application.statistics;

import ch.heigvd.amt.stack.application.authentication.AuthenticationFacade;
import ch.heigvd.amt.stack.application.authentication.command.RegisterCommand;
import ch.heigvd.amt.stack.application.statistics.query.UsageStatisticsQuery;
import ch.heigvd.amt.stack.infrastructure.persistence.memory.InMemoryAnswerRepository;
import ch.heigvd.amt.stack.infrastructure.persistence.memory.InMemoryCredentialRepository;
import ch.heigvd.amt.stack.infrastructure.persistence.memory.InMemoryQuestionRepository;
import ch.heigvd.amt.stack.infrastructure.persistence.memory.InMemorySessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatisticsFacadeIntegration {

    private AuthenticationFacade authenticationFacade;
    private StatisticsFacade statisticsFacade;

    @BeforeEach
    public void prepare() {
        var answers = new InMemoryAnswerRepository();
        var credentials = new InMemoryCredentialRepository();
        var questions = new InMemoryQuestionRepository();
        var sessions = new InMemorySessionRepository();

        this.authenticationFacade = new AuthenticationFacade(credentials, sessions);
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
}

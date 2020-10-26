package ch.heigvd.amt.stack.application.statistics;

import ch.heigvd.amt.stack.application.statistics.query.UsageStatisticsQuery;
import ch.heigvd.amt.stack.infrastructure.persistence.memory.InMemoryAnswerRepository;
import ch.heigvd.amt.stack.infrastructure.persistence.memory.InMemoryCredentialRepository;
import ch.heigvd.amt.stack.infrastructure.persistence.memory.InMemoryQuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatisticsFacadeIntegration {

    private StatisticsFacade statisticsFacade;

    @BeforeEach
    public void prepare() {
        var answers = new InMemoryAnswerRepository();
        var credentials = new InMemoryCredentialRepository();
        var questions = new InMemoryQuestionRepository();

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
}

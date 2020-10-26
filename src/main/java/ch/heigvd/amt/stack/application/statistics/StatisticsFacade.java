package ch.heigvd.amt.stack.application.statistics;

import ch.heigvd.amt.stack.application.statistics.dto.UsageStatisticsDTO;
import ch.heigvd.amt.stack.application.statistics.query.UsageStatisticsQuery;
import ch.heigvd.amt.stack.domain.answer.AnswerRepository;
import ch.heigvd.amt.stack.domain.authentication.CredentialRepository;
import ch.heigvd.amt.stack.domain.question.QuestionRepository;

import javax.inject.Inject;

public class StatisticsFacade {

    private final AnswerRepository answerRepository;
    private final CredentialRepository credentialRepository;
    private final QuestionRepository questionRepository;

    @Inject
    public StatisticsFacade(
            AnswerRepository answers,
            CredentialRepository credentials,
            QuestionRepository questions
    ) {
        this.answerRepository = answers;
        this.credentialRepository = credentials;
        this.questionRepository = questions;
    }

    public UsageStatisticsDTO getUsageStatistics(UsageStatisticsQuery query) {
        return UsageStatisticsDTO.builder()
                .answerCount(0)
                .questionCount(0)
                .userCount(0)
                .build();
    }
}

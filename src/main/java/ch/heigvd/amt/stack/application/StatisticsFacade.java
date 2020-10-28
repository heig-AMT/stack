package ch.heigvd.amt.stack.application;

import ch.heigvd.amt.stack.application.statistics.dto.UsageStatisticsDTO;
import ch.heigvd.amt.stack.application.statistics.query.UsageStatisticsQuery;
import ch.heigvd.amt.stack.domain.answer.AnswerRepository;
import ch.heigvd.amt.stack.domain.authentication.CredentialRepository;
import ch.heigvd.amt.stack.domain.question.QuestionRepository;

import javax.inject.Inject;

public class StatisticsFacade {

    @Inject
    AnswerRepository answerRepository;

    @Inject
    CredentialRepository credentialRepository;

    @Inject
    QuestionRepository questionRepository;

    public UsageStatisticsDTO getUsageStatistics(UsageStatisticsQuery ignored) {
        return UsageStatisticsDTO.builder()
                .answerCount(answerRepository.size())
                .questionCount(questionRepository.size())
                .userCount(credentialRepository.size())
                .build();
    }
}

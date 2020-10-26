package ch.heigvd.amt.stack.application.statistics.dto;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class UsageStatisticsDTO {
    long answerCount;
    long questionCount;
    long userCount;
}

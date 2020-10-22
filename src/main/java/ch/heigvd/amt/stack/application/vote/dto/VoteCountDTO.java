package ch.heigvd.amt.stack.application.vote.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class VoteCountDTO {
    int positiveCount;
    int negativeCount;
}

package ch.heigvd.amt.stack.application.vote.dto;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class VoteCountDTO {
    long count;
}

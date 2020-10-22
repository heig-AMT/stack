package ch.heigvd.amt.stack.application.answer.dto;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Builder
@Value
public class AnswerDTO {
    String author;
    String body;
    Instant creation;
    int positiveVotesCount;
    int negativeVotesCount;
}

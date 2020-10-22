package ch.heigvd.amt.stack.application.answer.dto;

import ch.heigvd.amt.stack.domain.answer.AnswerId;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Builder
@Value
public class AnswerDTO {
    String author;
    String body;
    Instant creation;
    AnswerId id;
    int positiveVotesCount;
    int negativeVotesCount;
}

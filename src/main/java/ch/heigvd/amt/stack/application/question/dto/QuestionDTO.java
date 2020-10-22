package ch.heigvd.amt.stack.application.question.dto;

import ch.heigvd.amt.stack.domain.question.QuestionId;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Builder
@Value
public class QuestionDTO {
    String author;
    String title;
    String description;
    Instant creation;
    QuestionStatusDTO status;
    QuestionId id;
}

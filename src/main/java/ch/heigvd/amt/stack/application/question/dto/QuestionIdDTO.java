package ch.heigvd.amt.stack.application.question.dto;

import ch.heigvd.amt.stack.domain.question.QuestionId;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class QuestionIdDTO {
    QuestionId id;
}

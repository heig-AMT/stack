package ch.heigvd.amt.stack.application.question.query;

import ch.heigvd.amt.stack.domain.question.QuestionId;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class SingleQuestionQuery {
    QuestionId id;
    String tag;
}

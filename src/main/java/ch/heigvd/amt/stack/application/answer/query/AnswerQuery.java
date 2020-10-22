package ch.heigvd.amt.stack.application.answer.query;

import ch.heigvd.amt.stack.domain.question.QuestionId;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AnswerQuery {
    QuestionId forQuestion;
    String tag;
}

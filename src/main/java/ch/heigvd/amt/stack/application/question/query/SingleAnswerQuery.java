package ch.heigvd.amt.stack.application.question.query;

import ch.heigvd.amt.stack.domain.answer.AnswerId;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class SingleAnswerQuery {
    AnswerId id;
    String tag;
}

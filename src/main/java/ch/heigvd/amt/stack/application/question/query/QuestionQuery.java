package ch.heigvd.amt.stack.application.question.query;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class QuestionQuery {
    String shouldContain;
    String tag;
}

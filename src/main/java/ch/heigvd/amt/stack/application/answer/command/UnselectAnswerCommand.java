package ch.heigvd.amt.stack.application.answer.command;

import ch.heigvd.amt.stack.domain.question.QuestionId;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class UnselectAnswerCommand {
    QuestionId forQuestion;
    String tag;
}

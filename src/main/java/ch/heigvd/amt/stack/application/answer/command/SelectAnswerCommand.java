package ch.heigvd.amt.stack.application.answer.command;

import ch.heigvd.amt.stack.domain.answer.AnswerId;
import ch.heigvd.amt.stack.domain.question.QuestionId;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class SelectAnswerCommand {
    QuestionId forQuestion;
    AnswerId answer;
    String tag;
}

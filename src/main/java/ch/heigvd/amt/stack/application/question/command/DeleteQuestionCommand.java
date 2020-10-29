package ch.heigvd.amt.stack.application.question.command;

import ch.heigvd.amt.stack.domain.question.QuestionId;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DeleteQuestionCommand {
    QuestionId question;
    String tag;
}

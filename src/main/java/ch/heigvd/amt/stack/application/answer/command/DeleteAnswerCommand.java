package ch.heigvd.amt.stack.application.answer.command;

import ch.heigvd.amt.stack.domain.answer.AnswerId;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DeleteAnswerCommand {
    AnswerId answer;
    String tag;
}

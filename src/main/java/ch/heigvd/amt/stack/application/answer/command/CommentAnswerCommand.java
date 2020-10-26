package ch.heigvd.amt.stack.application.answer.command;

import ch.heigvd.amt.stack.domain.answer.AnswerId;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class CommentAnswerCommand {
    AnswerId answer;
    String body;
    String tag;
}

package ch.heigvd.amt.stack.application.question.command;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class AskQuestionCommand {
    String title;
    String description;
    String tag;
}

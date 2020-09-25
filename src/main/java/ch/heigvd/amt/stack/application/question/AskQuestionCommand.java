package ch.heigvd.amt.stack.application.question;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class AskQuestionCommand {
    String author;
    String title;
    String description;
}

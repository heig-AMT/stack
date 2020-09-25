package ch.heigvd.amt.stack.domain.question;

import ch.heigvd.amt.stack.domain.Entity;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Question implements Entity {

    @Builder.Default
    QuestionId id = QuestionId.create();
    String title;
    String description;
}

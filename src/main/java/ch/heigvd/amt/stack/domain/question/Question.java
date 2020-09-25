package ch.heigvd.amt.stack.domain.question;

import ch.heigvd.amt.stack.domain.Entity;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Question implements Entity<QuestionId> {

    @Builder.Default
    QuestionId id = QuestionId.create();
    String author;
    String title;
    String description;
}

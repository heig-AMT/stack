package ch.heigvd.amt.stack.domain.question;

import ch.heigvd.amt.stack.domain.Entity;
import ch.heigvd.amt.stack.domain.authentication.CredentialId;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class Question implements Entity<QuestionId> {

    @Builder.Default
    QuestionId id = QuestionId.create();
    CredentialId author;
    String title;
    String description;
    Instant creation;
}

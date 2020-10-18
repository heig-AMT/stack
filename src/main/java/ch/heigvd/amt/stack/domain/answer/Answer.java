package ch.heigvd.amt.stack.domain.answer;

import ch.heigvd.amt.stack.domain.Entity;
import ch.heigvd.amt.stack.domain.authentication.CredentialId;
import ch.heigvd.amt.stack.domain.question.QuestionId;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class Answer implements Entity<AnswerId> {
    @Builder.Default
    AnswerId idAnswer = AnswerId.create();
    QuestionId idxQuestion;
    CredentialId idxCredential;

    String description;
    Instant creation;

    @Override
    public AnswerId getId()
    {
        return idAnswer;
    }
}

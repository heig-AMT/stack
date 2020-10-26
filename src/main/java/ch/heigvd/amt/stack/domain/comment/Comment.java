package ch.heigvd.amt.stack.domain.comment;

import ch.heigvd.amt.stack.domain.Entity;
import ch.heigvd.amt.stack.domain.answer.AnswerId;
import ch.heigvd.amt.stack.domain.authentication.CredentialId;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Comment implements Entity<CommentId> {

    @Builder.Default
    CommentId id = CommentId.create();
    AnswerId answer;
    CredentialId creator;

    String contents;
}

package ch.heigvd.amt.stack.application.answer.command;

import ch.heigvd.amt.stack.domain.comment.CommentId;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DeleteCommentCommand {
    CommentId comment;
    String tag;
}

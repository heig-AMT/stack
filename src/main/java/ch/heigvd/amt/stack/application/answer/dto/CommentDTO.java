package ch.heigvd.amt.stack.application.answer.dto;

import ch.heigvd.amt.stack.domain.comment.CommentId;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Builder
@Value
public class CommentDTO {
    String author;
    String contents;
    Instant creation;
    CommentId id;
    boolean deletionEnabled;
}

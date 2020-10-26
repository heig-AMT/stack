package ch.heigvd.amt.stack.application.answer.dto;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Builder
@Value
public class CommentDTO {
    String author;
    String contents;
    Instant creation;
    boolean deletionEnabled;
}

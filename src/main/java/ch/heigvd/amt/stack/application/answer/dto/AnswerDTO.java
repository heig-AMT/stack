package ch.heigvd.amt.stack.application.answer.dto;

import ch.heigvd.amt.stack.domain.answer.AnswerId;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.List;

@Builder
@Value
public class AnswerDTO {
    String author;
    String body;
    Instant creation;
    AnswerId id;
    List<CommentDTO> comments;
    int positiveVotesCount;
    int negativeVotesCount;
    boolean hasPositiveVote;
    boolean hasNegativeVote;
    boolean deletionEnabled;
    boolean selectionEnabled;
    boolean selected;
}

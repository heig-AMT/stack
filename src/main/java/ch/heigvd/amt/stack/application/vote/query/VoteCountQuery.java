package ch.heigvd.amt.stack.application.vote.query;

import ch.heigvd.amt.stack.domain.answer.AnswerId;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class VoteCountQuery {
    AnswerId forAnswer;
    boolean isUpvote;
}

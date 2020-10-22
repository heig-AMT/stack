package ch.heigvd.amt.stack.domain.vote;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Vote {

   VoteId id;
   boolean isUpvote;
}

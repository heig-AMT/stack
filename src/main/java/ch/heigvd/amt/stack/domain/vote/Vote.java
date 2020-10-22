package ch.heigvd.amt.stack.domain.vote;

import ch.heigvd.amt.stack.domain.Entity;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Vote implements Entity<VoteId> {

   VoteId id;
   boolean isUpvote;
}

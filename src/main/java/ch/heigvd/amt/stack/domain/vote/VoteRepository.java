package ch.heigvd.amt.stack.domain.vote;

import ch.heigvd.amt.stack.application.vote.query.VoteCountQuery;
//import ch.heigvd.amt.stack.application.utils.Pair;
import ch.heigvd.amt.stack.domain.Repository;
import org.apache.commons.lang3.tuple.Pair;

public interface VoteRepository extends Repository<Vote, VoteId> {
    public Pair<Integer, Integer> countUpAndDownVotes(VoteCountQuery query);
}

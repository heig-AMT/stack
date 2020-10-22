package ch.heigvd.amt.stack.domain.vote;

import ch.heigvd.amt.stack.application.answer.query.VoteCountQuery;
import ch.heigvd.amt.stack.domain.Repository;

public interface VoteRepository extends Repository<Vote, VoteId> {

    /**
     * Returns the count of votes that match a certain predicate.
     *
     * @param query the query predicate for votes.
     * @return how many votes match the predicate.
     */
    default int count(VoteCountQuery query) {
        return (int) findAll().stream()
                .filter(vote -> vote.getId().getAnswer().equals(query.getForAnswer()))
                .filter(vote -> vote.isUpvote() == query.isUpvote())
                .count();
    }
}

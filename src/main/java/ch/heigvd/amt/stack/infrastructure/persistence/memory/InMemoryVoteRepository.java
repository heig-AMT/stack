package ch.heigvd.amt.stack.infrastructure.persistence.memory;

import ch.heigvd.amt.stack.domain.vote.Vote;
import ch.heigvd.amt.stack.domain.vote.VoteId;
import ch.heigvd.amt.stack.domain.vote.VoteRepository;

public class InMemoryVoteRepository extends InMemoryRepository<Vote, VoteId> implements VoteRepository {
}

package ch.heigvd.amt.stack.infrastructure.persistence.memory;

import ch.heigvd.amt.stack.application.answer.query.VoteCountQuery;
import ch.heigvd.amt.stack.domain.answer.AnswerId;
import ch.heigvd.amt.stack.domain.authentication.CredentialId;
import ch.heigvd.amt.stack.domain.vote.Vote;
import ch.heigvd.amt.stack.domain.vote.VoteId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryVoteRepositoryTest {

    @Test
    public void testEmptyRepositoryHasNoVotes() {
        var repository = new InMemoryVoteRepository();
        var down = repository.count(VoteCountQuery.builder()
                .forAnswer(AnswerId.create())
                .isUpvote(false)
                .build());
        var up = repository.count(VoteCountQuery.builder()
                .forAnswer(AnswerId.create())
                .isUpvote(true)
                .build());

        assertEquals(0, down);
        assertEquals(0, up);
    }

    @Test
    public void testRepositoryMakesDistinctionBetweenUpvoteAndDownvote() {
        var repository = new InMemoryVoteRepository();
        var answer = AnswerId.create();

        var alice = CredentialId.create();
        var bob = CredentialId.create();

        repository.save(Vote.builder()
                .id(VoteId.builder().answer(answer).voter(alice).build())
                .isUpvote(true)
                .build());

        repository.save(Vote.builder()
                .id(VoteId.builder().answer(answer).voter(bob).build())
                .isUpvote(true)
                .build());

        var down = repository.count(VoteCountQuery.builder()
                .forAnswer(answer)
                .isUpvote(false)
                .build());
        var up = repository.count(VoteCountQuery.builder()
                .forAnswer(answer)
                .isUpvote(true)
                .build());

        assertEquals(0, down);
        assertEquals(2, up);
    }

    @Test
    public void testRepositoryDoesNotAllowDoubleVotes() {
        var repository = new InMemoryVoteRepository();
        var answer = AnswerId.create();

        var alice = CredentialId.create();
        int up = 1, down = 1;

        // Upvote

        repository.save(Vote.builder()
                .id(VoteId.builder().answer(answer).voter(alice).build())
                .isUpvote(true)
                .build());

        repository.save(Vote.builder()
                .id(VoteId.builder().answer(answer).voter(alice).build())
                .isUpvote(true)
                .build());

        up = repository.count(VoteCountQuery.builder()
                .forAnswer(answer)
                .isUpvote(true)
                .build());

        assertEquals(1, up);

        // Downvote

        repository.save(Vote.builder()
                .id(VoteId.builder().answer(answer).voter(alice).build())
                .isUpvote(false)
                .build());

        down = repository.count(VoteCountQuery.builder()
                .forAnswer(answer)
                .isUpvote(false)
                .build());

        up = repository.count(VoteCountQuery.builder()
                .forAnswer(answer)
                .isUpvote(true)
                .build());

        assertEquals(0, up);
        assertEquals(1, down);
    }
}

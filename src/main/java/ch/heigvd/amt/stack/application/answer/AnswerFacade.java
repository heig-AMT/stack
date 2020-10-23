package ch.heigvd.amt.stack.application.answer;

import ch.heigvd.amt.stack.application.answer.command.AnswerQuestionCommand;
import ch.heigvd.amt.stack.application.answer.command.DeleteAnswerCommand;
import ch.heigvd.amt.stack.application.answer.command.DownvoteAnswerCommand;
import ch.heigvd.amt.stack.application.answer.command.UpvoteAnswerCommand;
import ch.heigvd.amt.stack.application.answer.dto.AnswerDTO;
import ch.heigvd.amt.stack.application.answer.dto.AnswerListDTO;
import ch.heigvd.amt.stack.application.answer.query.AnswerQuery;
import ch.heigvd.amt.stack.application.authentication.query.SessionQuery;
import ch.heigvd.amt.stack.application.answer.query.VoteCountQuery;
import ch.heigvd.amt.stack.domain.answer.Answer;
import ch.heigvd.amt.stack.domain.answer.AnswerNotFoundException;
import ch.heigvd.amt.stack.domain.answer.AnswerRepository;
import ch.heigvd.amt.stack.domain.authentication.AuthenticationFailedException;
import ch.heigvd.amt.stack.domain.authentication.CredentialRepository;
import ch.heigvd.amt.stack.domain.authentication.Session;
import ch.heigvd.amt.stack.domain.authentication.SessionRepository;
import ch.heigvd.amt.stack.domain.question.QuestionNotFoundException;
import ch.heigvd.amt.stack.domain.question.QuestionRepository;
import ch.heigvd.amt.stack.domain.vote.Vote;
import ch.heigvd.amt.stack.domain.vote.VoteId;
import ch.heigvd.amt.stack.domain.vote.VoteRepository;

import javax.inject.Inject;
import java.time.Instant;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

public class AnswerFacade {

    private final CredentialRepository credentialRepository;
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final SessionRepository sessionRepository;
    private final VoteRepository voteRepository;

    @Inject
    public AnswerFacade(
            CredentialRepository credentials,
            AnswerRepository answers,
            QuestionRepository questions,
            SessionRepository sessions,
            VoteRepository votes
    ) {
        this.credentialRepository = credentials;
        this.answerRepository = answers;
        this.questionRepository = questions;
        this.sessionRepository = sessions;
        this.voteRepository = votes;
    }

    /**
     * Answers a certain question, provided that the user is properly authenticated and that the question they want to
     * answer to actually exists.
     *
     * @param command the {@link AnswerQuestionCommand} that should be fulfilled.
     * @throws AuthenticationFailedException if the user is not properly authenticated.
     * @throws QuestionNotFoundException     if the question does not actually exist.
     */
    public void answer(AnswerQuestionCommand command) throws AuthenticationFailedException, QuestionNotFoundException {
        var session = sessionRepository.findBy(SessionQuery.builder()
                .tag(command.getTag())
                .build())
                .orElseThrow(AuthenticationFailedException::new);
        var question = questionRepository.findById(command.getQuestion())
                .orElseThrow(QuestionNotFoundException::new);
        answerRepository.save(
                Answer.builder()
                        .question(question.getId())
                        .creation(Instant.now())
                        .body(command.getBody())
                        .creator(session.getUser())
                        .build()
        );
    }

    /**
     * Deletes a certain answer, provided that the user is properly authenticated and actually has the necessary
     * permissions to delete the answer. To do that, users should be the owner of the said answer.
     *
     * @param command the {@link DeleteAnswerCommand} that should be fulfilled.
     * @throws AuthenticationFailedException if the user is not the question owner or is not authenticated.
     * @throws AnswerNotFoundException       if the answer does not exist.
     */
    // TODO : Integration test this.
    public void delete(DeleteAnswerCommand command) throws AuthenticationFailedException, AnswerNotFoundException {
        var session = sessionRepository.findBy(SessionQuery.builder()
                .tag(command.getTag())
                .build())
                .orElseThrow(AuthenticationFailedException::new);
        var answer = answerRepository.findById(command.getAnswer())
                .orElseThrow(AnswerNotFoundException::new);
        if (!session.getUser().equals(answer.getCreator())) {
            throw new AuthenticationFailedException();
        }
        answerRepository.remove(answer.getId());
    }

    /**
     * Upvote a certain answer, provided that the user is properly authenticated.
     *
     * @param command the {@link UpvoteAnswerCommand} that should be fulfilled.
     * @throws AuthenticationFailedException if the user is not properly authenticated.
     */
    public void upvote(UpvoteAnswerCommand command) throws AuthenticationFailedException {
        var session = sessionRepository.findBy(SessionQuery.builder()
                .tag(command.getTag())
                .build())
                .orElseThrow(AuthenticationFailedException::new);
        voteRepository.save(Vote.builder()
                .id(VoteId.builder()
                        .voter(session.getUser())
                        .answer(command.getAnswer())
                        .build())
                .isUpvote(true)
                .build());
    }

    /**
     * Downvote a certain answer, provided that the user is properly authenticated.
     *
     * @param command the {@link DownvoteAnswerCommand} that should be fulfilled.
     * @throws AuthenticationFailedException if the user is not properly authenticated.
     */
    public void downvote(DownvoteAnswerCommand command) throws AuthenticationFailedException {
        var session = sessionRepository.findBy(SessionQuery.builder()
                .tag(command.getTag())
                .build())
                .orElseThrow(AuthenticationFailedException::new);
        voteRepository.save(Vote.builder()
                .id(VoteId.builder()
                        .voter(session.getUser())
                        .answer(command.getAnswer())
                        .build())
                .isUpvote(false)
                .build());
    }

    /**
     * Returns a list of all the answers for a certain question.
     *
     * @param query the {@link AnswerQuery} that should be fulfilled.
     * @return an {@link AnswerListDTO} with all answers for the query.
     */
    public AnswerListDTO getAnswers(AnswerQuery query) {
        var user = Optional.ofNullable(query.getTag()).stream()
                .flatMap(tag -> sessionRepository.findBy(SessionQuery.builder()
                        .tag(tag)
                        .build()).stream())
                .map(Session::getUser)
                .findAny();
        var answers = answerRepository.findBy(query).stream()
                .map(answer -> AnswerDTO.builder()
                        .author(credentialRepository.findById(answer.getCreator()).get().getUsername())
                        .body(answer.getBody())
                        .creation(answer.getCreation())
                        .id(answer.getId())
                        .positiveVotesCount(voteRepository.count(VoteCountQuery.builder()
                                .forAnswer(answer.getId())
                                .isUpvote(true)
                                .build()))
                        .negativeVotesCount(voteRepository.count(VoteCountQuery.builder()
                                .forAnswer(answer.getId())
                                .isUpvote(false)
                                .build()))
                        .hasPositiveVote(
                                user.isPresent() && voteRepository.findById(VoteId.builder()
                                        .answer(answer.getId())
                                        .voter(user.get())
                                        .build())
                                        // True if user has upvote.
                                        .map(Vote::isUpvote)
                                        // Otherwise nothing
                                        .orElse(false)
                        )
                        .hasNegativeVote(
                                user.isPresent() && voteRepository.findById(VoteId.builder()
                                        .answer(answer.getId())
                                        .voter(user.get())
                                        .build())
                                        // True if user has downvote.
                                        .map(Vote::isUpvote)
                                        .map(up -> !up)
                                        // Otherwise nothing.
                                        .orElse(false)
                        )
                        .deletionEnabled(user.isPresent() && user.get().equals(answer.getCreator()))
                        .build()
                )
                .sorted(Comparator.comparing(a -> a.getNegativeVotesCount() - a.getPositiveVotesCount()))
                .collect(Collectors.toUnmodifiableList());
        return AnswerListDTO.builder()
                .answers(answers)
                .build();
    }
}

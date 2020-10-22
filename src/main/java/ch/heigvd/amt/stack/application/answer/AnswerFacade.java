package ch.heigvd.amt.stack.application.answer;

import ch.heigvd.amt.stack.application.answer.command.AnswerQuestionCommand;
import ch.heigvd.amt.stack.application.answer.command.DownvoteAnswerCommand;
import ch.heigvd.amt.stack.application.answer.command.UpvoteAnswerCommand;
import ch.heigvd.amt.stack.application.answer.dto.AnswerDTO;
import ch.heigvd.amt.stack.application.answer.dto.AnswerListDTO;
import ch.heigvd.amt.stack.application.answer.query.AnswerQuery;
import ch.heigvd.amt.stack.application.authentication.query.SessionQuery;
import ch.heigvd.amt.stack.application.answer.query.VoteCountQuery;
import ch.heigvd.amt.stack.domain.answer.Answer;
import ch.heigvd.amt.stack.domain.answer.AnswerRepository;
import ch.heigvd.amt.stack.domain.authentication.AuthenticationFailedException;
import ch.heigvd.amt.stack.domain.authentication.CredentialRepository;
import ch.heigvd.amt.stack.domain.authentication.SessionRepository;
import ch.heigvd.amt.stack.domain.question.QuestionNotFoundException;
import ch.heigvd.amt.stack.domain.question.QuestionRepository;
import ch.heigvd.amt.stack.domain.vote.Vote;
import ch.heigvd.amt.stack.domain.vote.VoteId;
import ch.heigvd.amt.stack.domain.vote.VoteRepository;

import javax.inject.Inject;
import java.time.Instant;
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
     * Upvote a certain answer, provided that the user is properly authenticated.
     *
     * @param command the {@link UpvoteAnswerCommand} that should be fulfilled.
     * @throws AuthenticationFailedException if the user is not properly authenticated.
     */
    // TODO : Integration test this.
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
    // TODO : Integration test this.
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
                        .build()
                )
                .collect(Collectors.toUnmodifiableList());
        return AnswerListDTO.builder()
                .answers(answers)
                .build();
    }
}

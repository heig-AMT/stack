package ch.heigvd.amt.stack.application;

import ch.heigvd.amt.stack.application.answer.command.*;
import ch.heigvd.amt.stack.application.answer.dto.AnswerDTO;
import ch.heigvd.amt.stack.application.answer.dto.AnswerListDTO;
import ch.heigvd.amt.stack.application.answer.dto.CommentDTO;
import ch.heigvd.amt.stack.application.answer.query.AnswerQuery;
import ch.heigvd.amt.stack.application.answer.query.CommentQuery;
import ch.heigvd.amt.stack.application.answer.query.VoteCountQuery;
import ch.heigvd.amt.stack.application.authentication.query.SessionQuery;
import ch.heigvd.amt.stack.domain.answer.Answer;
import ch.heigvd.amt.stack.domain.answer.AnswerId;
import ch.heigvd.amt.stack.domain.answer.AnswerNotFoundException;
import ch.heigvd.amt.stack.domain.answer.AnswerRepository;
import ch.heigvd.amt.stack.domain.authentication.*;
import ch.heigvd.amt.stack.domain.comment.Comment;
import ch.heigvd.amt.stack.domain.comment.CommentNotFoundException;
import ch.heigvd.amt.stack.domain.comment.CommentRepository;
import ch.heigvd.amt.stack.domain.question.QuestionNotFoundException;
import ch.heigvd.amt.stack.domain.question.QuestionRepository;
import ch.heigvd.amt.stack.domain.vote.Vote;
import ch.heigvd.amt.stack.domain.vote.VoteId;
import ch.heigvd.amt.stack.domain.vote.VoteRepository;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.time.Instant;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@RequestScoped
public class AnswerFacade {

    @Inject
    CommentRepository commentRepository;

    @Inject
    CredentialRepository credentialRepository;

    @Inject
    AnswerRepository answerRepository;

    @Inject
    QuestionRepository questionRepository;

    @Inject
    SessionRepository sessionRepository;

    @Inject
    VoteRepository voteRepository;

    private final BiFunction<Boolean, Comment, CommentDTO> commentToDTO = new BiFunction<>() {
        @Override
        public CommentDTO apply(Boolean deletionEnabled, Comment comment) {
            return CommentDTO.builder()
                    .author(credentialRepository.findById(comment.getCreator()).map(Credential::getUsername).get())
                    .creation(comment.getCreation())
                    .contents(comment.getContents())
                    .id(comment.getId())
                    .deletionEnabled(deletionEnabled) // true if we are the creator.
                    .build();
        }
    };

    /**
     * Answers a certain question, provided that the user is properly authenticated and that the question they want to
     * answer to actually exists.
     *
     * @param command the {@link AnswerQuestionCommand} that should be fulfilled.
     * @throws AuthenticationFailedException if the user is not properly authenticated.
     * @throws QuestionNotFoundException     if the question does not actually exist.
     */
    public AnswerId answer(AnswerQuestionCommand command) throws AuthenticationFailedException, QuestionNotFoundException {
        var session = sessionRepository.findBy(SessionQuery.builder()
                .tag(command.getTag())
                .build())
                .orElseThrow(AuthenticationFailedException::new);
        var question = questionRepository.findById(command.getQuestion())
                .orElseThrow(QuestionNotFoundException::new);
        var id = AnswerId.create();
        answerRepository.save(
                Answer.builder()
                        .id(id)
                        .question(question.getId())
                        .creation(Instant.now())
                        .body(command.getBody())
                        .creator(session.getUser())
                        .build()
        );
        return id;
    }

    /**
     * Comments a certain answer, provided that the user is properly authenticated and that the answer they want to
     * comment on actually exists.
     *
     * @param command the {@link CommentAnswerCommand} that should be fulfilled.
     * @throws AuthenticationFailedException if the user is not properly authenticated.
     * @throws AnswerNotFoundException       if the answer does not actually exist.
     */
    public void comment(CommentAnswerCommand command) throws AuthenticationFailedException, AnswerNotFoundException {
        var session = sessionRepository.findBy(SessionQuery.builder()
                .tag(command.getTag())
                .build())
                .orElseThrow(AuthenticationFailedException::new);
        var answer = answerRepository.findById(command.getAnswer())
                .orElseThrow(AnswerNotFoundException::new);
        commentRepository.save(
                Comment.builder()
                        .answer(answer.getId())
                        .creation(Instant.now())
                        .contents(command.getBody())
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
     * Deletes a certain comment, provided that the user is the creator of the comment or the creator of the question
     * that was answered to.
     *
     * @param command the {@link DeleteCommentCommand} that should be fulfilled.
     * @throws AuthenticationFailedException if the user is not the owner or is not authenticated.
     * @throws AnswerNotFoundException       if the answer of the comment does not exist.
     * @throws CommentNotFoundException      if the comment does not exist.
     */
    public void deleteComment(DeleteCommentCommand command) throws
            AuthenticationFailedException, AnswerNotFoundException, CommentNotFoundException {
        var session = sessionRepository.findBy(SessionQuery.builder()
                .tag(command.getTag())
                .build())
                .orElseThrow(AuthenticationFailedException::new);
        var comment = commentRepository.findById(command.getComment())
                .orElseThrow(CommentNotFoundException::new);
        var answer = answerRepository.findById(comment.getAnswer())
                .orElseThrow(AnswerNotFoundException::new);

        // Let both the answer owner and the comment creator delete the comment.
        if (!session.getUser().equals(comment.getCreator()) && !session.getUser().equals(answer.getCreator())) {
            throw new AuthenticationFailedException();
        }
        commentRepository.remove(command.getComment());
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
                        .comments(
                                // Fetch all the comments for the given answer.
                                commentRepository.findBy(CommentQuery.builder()
                                        .forAnswer(answer.getId())
                                        .build()).stream()
                                        // Map all the individual comments.
                                        .map(comment -> {
                                            var credential = user.orElse(null);
                                            // Enable deletions for comments owned by this user or the answer creator.
                                            var deletionEnabled = credential != null && (
                                                    credential.equals(comment.getCreator()) ||
                                                            credential.equals(answer.getCreator())
                                            );
                                            return commentToDTO.apply(deletionEnabled, comment);
                                        }).collect(Collectors.toList()))
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

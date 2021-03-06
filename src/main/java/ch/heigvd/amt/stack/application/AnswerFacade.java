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
import ch.heigvd.amt.stack.domain.comment.CommentId;
import ch.heigvd.amt.stack.domain.comment.CommentNotFoundException;
import ch.heigvd.amt.stack.domain.comment.CommentRepository;
import ch.heigvd.amt.stack.domain.gamification.GamificationEvent;
import ch.heigvd.amt.stack.domain.gamification.GamificationRepository;
import ch.heigvd.amt.stack.domain.question.Question;
import ch.heigvd.amt.stack.domain.question.QuestionNotFoundException;
import ch.heigvd.amt.stack.domain.question.QuestionRepository;
import ch.heigvd.amt.stack.domain.vote.Vote;
import ch.heigvd.amt.stack.domain.vote.VoteId;
import ch.heigvd.amt.stack.domain.vote.VoteRepository;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.time.Instant;
import java.util.Comparator;
import java.util.Objects;
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
    @Inject
    GamificationRepository gamificationRepository;

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
     * An implementation of {@link Comparator} that takes care of ordering {@link AnswerDTO} instances based on their
     * selection status as well as their vote counts.
     */
    private static final Comparator<AnswerDTO> COMPARATOR = (f, s) -> {
        int comp = Boolean.compare(s.isSelected(), f.isSelected()); // reversed.
        if (comp != 0) {
            return comp;
        } else {
            return (s.getPositiveVotesCount() - s.getNegativeVotesCount()) -
                    (f.getPositiveVotesCount() - f.getNegativeVotesCount());
        }
    };

    /**
     * Returns the {@link Session} associated with a certain user tag, or throws an exception.
     */
    private Session requireSession(String tag) throws AuthenticationFailedException {
        return sessionRepository.findBy(SessionQuery.builder()
                .tag(tag)
                .build())
                .orElseThrow(AuthenticationFailedException::new);
    }

    /**
     * Answers a certain question, provided that the user is properly authenticated and that the question they want to
     * answer to actually exists.
     *
     * @param command the {@link AnswerQuestionCommand} that should be fulfilled.
     * @throws AuthenticationFailedException if the user is not properly authenticated.
     * @throws QuestionNotFoundException     if the question does not actually exist.
     */
    public AnswerId answer(AnswerQuestionCommand command) throws AuthenticationFailedException, QuestionNotFoundException {
        var session = requireSession(command.getTag());
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
        gamificationRepository.saveEvent(session.getUser(), GamificationEvent.NEW_ANSWER);
        return id;
    }

    /**
     * Comments a certain answer, provided that the user is properly authenticated and that the answer they want to
     * comment on actually exists.
     *
     * @param command the {@link CommentAnswerCommand} that should be fulfilled.
     * @return {@link CommentId} id of the comment created
     * @throws AuthenticationFailedException if the user is not properly authenticated.
     * @throws AnswerNotFoundException       if the answer does not actually exist.
     */
    public CommentId comment(CommentAnswerCommand command) throws AuthenticationFailedException, AnswerNotFoundException {
        var session = requireSession(command.getTag());
        var answer = answerRepository.findById(command.getAnswer())
                .orElseThrow(AnswerNotFoundException::new);
        CommentId id = CommentId.create();
        commentRepository.save(
                Comment.builder()
                        .id(id)
                        .answer(answer.getId())
                        .creation(Instant.now())
                        .contents(command.getBody())
                        .creator(session.getUser())
                        .build()
        );
        gamificationRepository.saveEvent(session.getUser(), GamificationEvent.NEW_COMMENT);
        return id;
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
        var session = requireSession(command.getTag());
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
        var session = requireSession(command.getTag());
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
        var session = requireSession(command.getTag());
        voteRepository.save(Vote.builder()
                .id(VoteId.builder()
                        .voter(session.getUser())
                        .answer(command.getAnswer())
                        .build())
                .isUpvote(true)
                .build());
        gamificationRepository.saveEvent(session.getUser(), GamificationEvent.UPVOTE);
    }

    /**
     * Downvote a certain answer, provided that the user is properly authenticated.
     *
     * @param command the {@link DownvoteAnswerCommand} that should be fulfilled.
     * @throws AuthenticationFailedException if the user is not properly authenticated.
     */
    public void downvote(DownvoteAnswerCommand command) throws AuthenticationFailedException {
        var session = requireSession(command.getTag());
        voteRepository.save(Vote.builder()
                .id(VoteId.builder()
                        .voter(session.getUser())
                        .answer(command.getAnswer())
                        .build())
                .isUpvote(false)
                .build());
        gamificationRepository.saveEvent(session.getUser(), GamificationEvent.DOWNVOTE);
    }

    /**
     * Selects a certain answer, provided that the user is properly authenticated.
     *
     * @param command the {@link SelectAnswerCommand} that should be fulfilled.
     * @throws AuthenticationFailedException if the user is not properly authenticated.
     * @throws QuestionNotFoundException     if the question could not be found.
     */
    public void select(SelectAnswerCommand command) throws AuthenticationFailedException, QuestionNotFoundException {
        var session = requireSession(command.getTag());
        var question = questionRepository.findById(command.getForQuestion())
                .orElseThrow(QuestionNotFoundException::new);

        if (!question.getAuthor().equals(session.getUser())) {
            throw new AuthenticationFailedException();
        }

        var updated = Question.builder()
                .author(question.getAuthor())
                .creation(question.getCreation())
                .description(question.getDescription())
                .id(question.getId())
                .title(question.getTitle())
                .selectedAnswer(command.getAnswer())
                .build();
        questionRepository.save(updated);
        gamificationRepository.saveEvent(session.getUser(), GamificationEvent.SELECTION);
    }

    /**
     * Unselects a certain answer, provided that the user is properly authenticated.
     *
     * @param command the {@link UnselectAnswerCommand} that should be fulfilled.
     * @throws AuthenticationFailedException if the user is not properly authenticated.
     * @throws QuestionNotFoundException     if the question couldd not be found.
     */
    public void unselect(UnselectAnswerCommand command) throws AuthenticationFailedException, QuestionNotFoundException {
        var session = requireSession(command.getTag());

        var question = questionRepository.findById(command.getForQuestion())
                .orElseThrow(QuestionNotFoundException::new);

        if (!question.getAuthor().equals(session.getUser())) {
            throw new AuthenticationFailedException();
        }

        var updated = Question.builder()
                .author(question.getAuthor())
                .creation(question.getCreation())
                .description(question.getDescription())
                .id(question.getId())
                .title(question.getTitle())
                .selectedAnswer(null)
                .build();
        questionRepository.save(updated);
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
        // Retrieve the question.
        var answers = answerRepository.findBy(query).stream()
                .map(answer -> {
                    var question = questionRepository.findById(answer.getQuestion());
                    return AnswerDTO.builder()
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
                            .selectionEnabled(question.isPresent() && question.map(Question::getAuthor).equals(user))
                            .selected(question.isPresent() && Objects.equals(question.get().getSelectedAnswer(), answer.getId()))
                            .build();
                })
                .sorted(COMPARATOR)
                .collect(Collectors.toUnmodifiableList());
        return AnswerListDTO.builder()
                .answers(answers)
                .build();
    }
}

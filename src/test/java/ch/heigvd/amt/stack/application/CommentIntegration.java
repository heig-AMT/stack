package ch.heigvd.amt.stack.application;

import ch.heigvd.amt.stack.application.answer.command.AnswerQuestionCommand;
import ch.heigvd.amt.stack.application.answer.command.CommentAnswerCommand;
import ch.heigvd.amt.stack.application.answer.command.DeleteCommentCommand;
import ch.heigvd.amt.stack.application.answer.query.AnswerQuery;
import ch.heigvd.amt.stack.application.authentication.command.RegisterCommand;
import ch.heigvd.amt.stack.application.question.command.AskQuestionCommand;
import ch.heigvd.amt.stack.domain.answer.AnswerId;
import ch.heigvd.amt.stack.domain.answer.AnswerNotFoundException;
import ch.heigvd.amt.stack.domain.authentication.AuthenticationFailedException;
import ch.heigvd.amt.stack.infrastructure.persistence.memory.*;
import ch.heigvd.amt.stack.infrastructure.persistence.mock.MockGamificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CommentIntegration {

    private AnswerFacade answerFacade;
    private AuthenticationFacade authenticationFacade;
    private QuestionFacade questionFacade;

    @BeforeEach
    public void prepare() {
        var comments = new InMemoryCommentRepository();
        var credentials = new InMemoryCredentialRepository();
        var answers = new InMemoryAnswerRepository();
        var questions = new InMemoryQuestionRepository();
        var sessions = new InMemorySessionRepository();
        var votes = new InMemoryVoteRepository();
        var games = new MockGamificationRepository();

        this.answerFacade = new AnswerFacade();
        this.answerFacade.answerRepository = answers;
        this.answerFacade.commentRepository = comments;
        this.answerFacade.credentialRepository = credentials;
        this.answerFacade.questionRepository = questions;
        this.answerFacade.sessionRepository = sessions;
        this.answerFacade.voteRepository = votes;
        this.answerFacade.gamificationRepository = games;

        this.authenticationFacade = new AuthenticationFacade();
        this.authenticationFacade.credentials = credentials;
        this.authenticationFacade.sessions = sessions;

        this.questionFacade = new QuestionFacade();
        this.questionFacade.answerRepository = answers;
        this.questionFacade.credentialRepository = credentials;
        this.questionFacade.sessionRepository = sessions;
        this.questionFacade.repository = questions;
        this.questionFacade.gamificationRepository = games;
    }

    @Test
    public void testRegisteredUserCanNotCommentOnMissingAnswer() {
        var register = RegisterCommand.builder()
                .username("alice")
                .password("password")
                .tag("tag")
                .build();
        authenticationFacade.register(register);

        var comment = CommentAnswerCommand.builder()
                .answer(AnswerId.create())
                .body("This is a stupid answer")
                .tag("tag")
                .build();

        assertThrows(AnswerNotFoundException.class, () -> {
            answerFacade.comment(comment);
        });
    }

    @Test
    public void testRegisteredUserCanCommentOnQuestion() {
        var register = RegisterCommand.builder()
                .username("alice")
                .password("password")
                .tag("tag")
                .build();
        authenticationFacade.register(register);

        var askQuestion = AskQuestionCommand.builder()
                .title("Title")
                .description("Description")
                .tag("tag")
                .build();

        var questionId = questionFacade.askQuestion(askQuestion);

        var answerQuestion = AnswerQuestionCommand.builder()
                .question(questionId)
                .body("This is a stupid question")
                .tag("tag")
                .build();

        assertDoesNotThrow(() -> {
            var answerId = answerFacade.answer(answerQuestion);
            var comment = CommentAnswerCommand.builder()
                    .answer(answerId)
                    .body("This is a stupid answer")
                    .tag("tag")
                    .build();
            answerFacade.comment(comment);

            var answers = answerFacade.getAnswers(AnswerQuery.builder()
                    .forQuestion(questionId)
                    .tag("tag")
                    .build());

            assertEquals(1, answers.getAnswers().size());
            var answerDTO = answers.getAnswers().get(0);
            assertEquals(1, answerDTO.getComments().size());
            var commentDTO = answerDTO.getComments().get(0);
            assertEquals("alice", commentDTO.getAuthor());
            assertEquals("This is a stupid answer", commentDTO.getContents());
            assertTrue(commentDTO.isDeletionEnabled());
        });

    }

    @Test
    public void testUnregisteredUserCanSeeCommentsButNotRemoveThem() {
        var register = RegisterCommand.builder()
                .username("alice")
                .password("password")
                .tag("tag")
                .build();
        authenticationFacade.register(register);

        var askQuestion = AskQuestionCommand.builder()
                .title("Title")
                .description("Description")
                .tag("tag")
                .build();

        var questionId = questionFacade.askQuestion(askQuestion);

        var answerQuestion = AnswerQuestionCommand.builder()
                .question(questionId)
                .body("This is a stupid question")
                .tag("tag")
                .build();

        assertDoesNotThrow(() -> {
            var answerId = answerFacade.answer(answerQuestion);
            var comment = CommentAnswerCommand.builder()
                    .answer(answerId)
                    .body("This is a stupid answer")
                    .tag("tag")
                    .build();
            answerFacade.comment(comment);

            // Unregistered call.
            var answers = answerFacade.getAnswers(AnswerQuery.builder()
                    .forQuestion(questionId)
                    .build());

            assertEquals(1, answers.getAnswers().size());
            var answerDTO = answers.getAnswers().get(0);
            assertEquals(1, answerDTO.getComments().size());
            var commentDTO = answerDTO.getComments().get(0);
            assertEquals("alice", commentDTO.getAuthor());
            assertEquals("This is a stupid answer", commentDTO.getContents());
            assertFalse(commentDTO.isDeletionEnabled());
        });
    }

    @Test
    public void testWrongUserCanNotRemoveComment() {

        final String tagAlice = "tagAlice";
        final String tagBob = "tagBob";

        var registerAlice = RegisterCommand.builder()
                .username("alice")
                .password("password")
                .tag(tagAlice)
                .build();

        var registerBob = RegisterCommand.builder()
                .username("bob")
                .password("password")
                .tag(tagBob)
                .build();
        authenticationFacade.register(registerAlice);
        authenticationFacade.register(registerBob);

        var askQuestion = AskQuestionCommand.builder()
                .title("Title")
                .description("Description")
                .tag(tagAlice)
                .build();

        var questionId = questionFacade.askQuestion(askQuestion);

        var answerQuestion = AnswerQuestionCommand.builder()
                .question(questionId)
                .body("This is a stupid question")
                .tag(tagAlice)
                .build();

        var answerId = assertDoesNotThrow(() ->
            answerFacade.answer(answerQuestion)
        );
            var comment = CommentAnswerCommand.builder()
                    .answer(answerId)
                    .body("This is a stupid answer")
                    .tag(tagAlice)
                    .build();

            var commentId = assertDoesNotThrow(() -> answerFacade.comment(comment));

        assertThrows(AuthenticationFailedException.class, () ->
                answerFacade.deleteComment(DeleteCommentCommand.builder()
                        .comment(commentId)
                        .tag(tagBob)
                        .build()));
    }

    @Test
    public void testRegisteredUserCanDeleteTheirComment() {
        var register = RegisterCommand.builder()
                .username("alice")
                .password("password")
                .tag("tag")
                .build();
        authenticationFacade.register(register);

        var askQuestion = AskQuestionCommand.builder()
                .title("Title")
                .description("Description")
                .tag("tag")
                .build();

        var questionId = questionFacade.askQuestion(askQuestion);

        var answerQuestion = AnswerQuestionCommand.builder()
                .question(questionId)
                .body("This is a stupid question")
                .tag("tag")
                .build();

        assertDoesNotThrow(() -> {
            var answerId = answerFacade.answer(answerQuestion);
            var comment = CommentAnswerCommand.builder()
                    .answer(answerId)
                    .body("This is a stupid answer")
                    .tag("tag")
                    .build();
            answerFacade.comment(comment);

            var answers = answerFacade.getAnswers(AnswerQuery.builder()
                    .forQuestion(questionId)
                    .tag("tag")
                    .build());
            var commentDTO = answers
                    .getAnswers().get(0)
                    .getComments().get(0);

            answerFacade.deleteComment(DeleteCommentCommand.builder()
                    .comment(commentDTO.getId())
                    .tag("tag")
                    .build());
        });
    }

    @Test
    public void testAnswerOwnerCanDeleteAComment() {
        var registerAlice = RegisterCommand.builder()
                .username("alice")
                .password("password")
                .tag("tagAlice")
                .build();
        var registerBob = RegisterCommand.builder()
                .username("bob")
                .password("password")
                .tag("tagBob")
                .build();
        authenticationFacade.register(registerAlice);
        authenticationFacade.register(registerBob);

        var askQuestion = AskQuestionCommand.builder()
                .title("Title")
                .description("Description")
                .tag("tagAlice")
                .build();

        var questionId = questionFacade.askQuestion(askQuestion);

        var answerQuestion = AnswerQuestionCommand.builder()
                .question(questionId)
                .body("This is a stupid question")
                .tag("tagBob")
                .build();

        assertDoesNotThrow(() -> {
            var answerId = answerFacade.answer(answerQuestion);
            var comment = CommentAnswerCommand.builder()
                    .answer(answerId)
                    .body("This is a stupid answer")
                    .tag("tagAlice")
                    .build();
            answerFacade.comment(comment);

            var answers = answerFacade.getAnswers(AnswerQuery.builder()
                    .forQuestion(questionId)
                    .tag("tagBob")
                    .build());
            var commentDTO = answers
                    .getAnswers().get(0)
                    .getComments().get(0);

            answerFacade.deleteComment(DeleteCommentCommand.builder()
                    .comment(commentDTO.getId())
                    .tag("tagBob")
                    .build());
        });

    }
}

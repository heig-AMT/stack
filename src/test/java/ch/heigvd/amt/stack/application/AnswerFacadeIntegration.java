package ch.heigvd.amt.stack.application;

import ch.heigvd.amt.stack.application.answer.command.*;
import ch.heigvd.amt.stack.application.answer.query.AnswerQuery;
import ch.heigvd.amt.stack.application.authentication.command.RegisterCommand;
import ch.heigvd.amt.stack.application.question.command.AskQuestionCommand;
import ch.heigvd.amt.stack.application.question.dto.QuestionDTO;
import ch.heigvd.amt.stack.application.question.query.SingleAnswerQuery;
import ch.heigvd.amt.stack.domain.authentication.AuthenticationFailedException;
import ch.heigvd.amt.stack.domain.question.QuestionId;
import ch.heigvd.amt.stack.domain.question.QuestionNotFoundException;
import ch.heigvd.amt.stack.infrastructure.persistence.memory.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class AnswerFacadeIntegration {

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

        this.answerFacade = new AnswerFacade();
        this.answerFacade.answerRepository = answers;
        this.answerFacade.commentRepository = comments;
        this.answerFacade.credentialRepository = credentials;
        this.answerFacade.questionRepository = questions;
        this.answerFacade.sessionRepository = sessions;
        this.answerFacade.voteRepository = votes;

        this.authenticationFacade = new AuthenticationFacade();
        this.authenticationFacade.credentials = credentials;
        this.authenticationFacade.sessions = sessions;

        this.questionFacade = new QuestionFacade();
        this.questionFacade.answerRepository = answers;
        this.questionFacade.credentialRepository = credentials;
        this.questionFacade.repository = questions;
        this.questionFacade.sessionRepository = sessions;
    }

    @Test
    public void testUnAuthenticatedUserCanNotAnswer() {
        var answer = AnswerQuestionCommand.builder()
                .question(QuestionId.create())
                .body("My wonderful answer")
                .tag("alice")
                .build();

        assertThrows(AuthenticationFailedException.class, () -> {
            this.answerFacade.answer(answer);
        });
    }

    @Test
    public void testAuthenticatedUserCanAnswerExistingQuestion() {
        var register = RegisterCommand.builder()
                .username("alice")
                .password("password")
                .tag("tag")
                .build();
        var ask = AskQuestionCommand.builder()
                .title("Title")
                .description("Description")
                .tag("tag")
                .build();

        authenticationFacade.register(register);
        var questionId = questionFacade.askQuestion(ask);

        var answer = AnswerQuestionCommand.builder()
                .question(questionId)
                .body("This is my answer.")
                .tag("tag")
                .build();

        var query = AnswerQuery.builder()
                .forQuestion(questionId)
                .build();

        assertDoesNotThrow(() -> {
            answerFacade.answer(answer);
            var result = answerFacade.getAnswers(query);
            assertEquals(1, result.getAnswers().size());
            var resultAnswer = result.getAnswers().get(0);
            assertEquals("alice", resultAnswer.getAuthor());
            assertEquals("This is my answer.", resultAnswer.getBody());
            assertEquals(0, resultAnswer.getPositiveVotesCount());
            assertEquals(0, resultAnswer.getNegativeVotesCount());
        });
    }

    @Test
    public void testAuthenticatedUserCanNotAnswerMissingQuestion() {
        var register = RegisterCommand.builder()
                .username("alice")
                .password("password")
                .tag("tag")
                .build();

        var answer = AnswerQuestionCommand.builder()
                .question(QuestionId.create())
                .body("This is a stupid question.")
                .tag("tag")
                .build();

        authenticationFacade.register(register);

        assertThrows(QuestionNotFoundException.class, () -> {
            answerFacade.answer(answer);
        });
    }

    @Test
    public void testOnlyAuthenticatedUserCanVote() {

        // Register.
        var register = RegisterCommand.builder()
                .username("alice")
                .password("password")
                .tag("tag")
                .build();
        var question = AskQuestionCommand.builder()
                .title("Title")
                .description("Description")
                .tag("tag")
                .build();
        authenticationFacade.register(register);

        // Ask a question and answer.
        var questionId = questionFacade.askQuestion(question);
        var answer = AnswerQuestionCommand.builder()
                .question(questionId)
                .body("This is a stupid question")
                .tag("tag")
                .build();
        assertDoesNotThrow(() -> answerFacade.answer(answer));

        // Get the only answer.
        var answers = answerFacade.getAnswers(AnswerQuery.builder()
                .tag("tag")
                .forQuestion(questionId)
                .build())
                .getAnswers();
        assertEquals(1, answers.size());

        // Upvote.
        var upvote = UpvoteAnswerCommand.builder()
                .answer(answers.get(0).getId())
                .tag("tag")
                .build();
        answerFacade.upvote(upvote);

        // Get the first answer count, and assert the vote succeeded.
        assertEquals(1, answerFacade.getAnswers(AnswerQuery.builder()
                .forQuestion(questionId)
                .build())
                .getAnswers().get(0).getPositiveVotesCount());

        // Upvote with non-authenticated account.
        assertThrows(AuthenticationFailedException.class, () -> {
            answerFacade.upvote(UpvoteAnswerCommand.builder()
                    .answer(answers.get(0).getId())
                    .tag("other")
                    .build());
        });

        // Get the first answer count, and assert the vote succeeded.
        assertEquals(1, answerFacade.getAnswers(AnswerQuery.builder()
                .forQuestion(questionId)
                .build())
                .getAnswers().get(0).getPositiveVotesCount());
    }

    @Test
    public void testOnlyAuthenticatedUserCanDownvote() {

        // Register.
        var register = RegisterCommand.builder()
                .username("alice")
                .password("password")
                .tag("tag")
                .build();
        var question = AskQuestionCommand.builder()
                .title("Title")
                .description("Description")
                .tag("tag")
                .build();
        authenticationFacade.register(register);

        // Ask a question and answer.
        var questionId = questionFacade.askQuestion(question);
        var answer = AnswerQuestionCommand.builder()
                .question(questionId)
                .body("This is a stupid question")
                .tag("tag")
                .build();
        assertDoesNotThrow(() -> answerFacade.answer(answer));

        // Get the only answer.
        var answers = answerFacade.getAnswers(AnswerQuery.builder()
                .tag("tag")
                .forQuestion(questionId)
                .build())
                .getAnswers();
        assertEquals(1, answers.size());

        // Downvote.
        var downvote = DownvoteAnswerCommand.builder()
                .answer(answers.get(0).getId())
                .tag("tag")
                .build();
        answerFacade.downvote(downvote);

        // Get the first answer count, and assert the vote succeeded.
        assertEquals(1, answerFacade.getAnswers(AnswerQuery.builder()
                .forQuestion(questionId)
                .build())
                .getAnswers().get(0).getNegativeVotesCount());

        // Downvote with non-authenticated account.
        assertThrows(AuthenticationFailedException.class, () -> {
            answerFacade.downvote(DownvoteAnswerCommand.builder()
                    .answer(answers.get(0).getId())
                    .tag("other")
                    .build());
        });

        // Get the first answer count, and assert the vote succeeded.
        assertEquals(1, answerFacade.getAnswers(AnswerQuery.builder()
                .forQuestion(questionId)
                .build())
                .getAnswers().get(0).getNegativeVotesCount());
    }

    @Test
    public void testAuthenticatedUserCanDeleteTheirOwnAnswer() {

        // Register.
        var register = RegisterCommand.builder()
                .username("alice")
                .password("password")
                .tag("tag")
                .build();
        var question = AskQuestionCommand.builder()
                .title("Title")
                .description("Description")
                .tag("tag")
                .build();
        authenticationFacade.register(register);

        // Ask a question and answer.
        var questionId = questionFacade.askQuestion(question);
        var answer = AnswerQuestionCommand.builder()
                .question(questionId)
                .body("This is a stupid question")
                .tag("tag")
                .build();
        assertDoesNotThrow(() -> answerFacade.answer(answer));

        // Make sure the question was added.
        var answers = answerFacade.getAnswers(AnswerQuery.builder()
                .tag("tag")
                .forQuestion(questionId)
                .build())
                .getAnswers();

        assertEquals(1, answers.size());
        assertTrue(answers.get(0).isDeletionEnabled());

        // Delete the answer.
        assertDoesNotThrow(() -> answerFacade.delete(DeleteAnswerCommand.builder()
                .tag("tag")
                .answer(answers.get(0).getId())
                .build()));

        // Check the question count.
        assertEquals(0, answerFacade.getAnswers(AnswerQuery.builder()
                .tag("tag")
                .forQuestion(questionId)
                .build())
                .getAnswers()
                .size());
    }

    @Test
    public void testUnauthenticatedUserCanNotDeleteAnAnswer() {

        // Register.
        var register = RegisterCommand.builder()
                .username("alice")
                .password("password")
                .tag("tag")
                .build();
        var question = AskQuestionCommand.builder()
                .title("Title")
                .description("Description")
                .tag("tag")
                .build();
        authenticationFacade.register(register);

        // Ask a question and answer.
        var questionId = questionFacade.askQuestion(question);
        var answer = AnswerQuestionCommand.builder()
                .question(questionId)
                .body("This is a stupid question")
                .tag("tag")
                .build();
        assertDoesNotThrow(() -> answerFacade.answer(answer));

        // Make sure the question was added.
        var answers = answerFacade.getAnswers(AnswerQuery.builder()
                .tag("anotherTag")
                .forQuestion(questionId)
                .build())
                .getAnswers();

        assertEquals(1, answers.size());
        assertFalse(answers.get(0).isDeletionEnabled());

        // Try to delete the answer.
        assertThrows(AuthenticationFailedException.class, () -> answerFacade.delete(DeleteAnswerCommand.builder()
                .answer(answers.get(0).getId())
                .build()));

        // Check the question count.
        assertEquals(1, answerFacade.getAnswers(AnswerQuery.builder()
                .tag("tag")
                .forQuestion(questionId)
                .build())
                .getAnswers()
                .size());
    }

    @Test
    public void testGettingQuestionFromAnswerReturnsCorrectQuestionDTO() {
        var register = RegisterCommand.builder()
                .username("alice")
                .password("password")
                .tag("tag")
                .build();
        var ask = AskQuestionCommand.builder()
                .title("What is love")
                .description("Description")
                .tag("tag")
                .build();

        authenticationFacade.register(register);
        QuestionId questionId = questionFacade.askQuestion(ask);

        var answer = AnswerQuestionCommand.builder()
                .body("Baby don't hurt me")
                .question(questionId)
                .tag("tag")
                .build();

        var answerId = assertDoesNotThrow(() -> answerFacade.answer(answer));

            Optional<QuestionDTO> questionDTO = questionFacade.getQuestion(SingleAnswerQuery.builder()
                    .id(answerId)
                    .tag("tag")
                    .build());

            assertEquals(questionId, questionDTO.get().getId());
    }

    @Test
    public void testQuestionOwnerCanSelectAnswerAsAccepted() {

        // Register.
        var register = RegisterCommand.builder()
                .username("alice")
                .password("password")
                .tag("tag")
                .build();
        var question = AskQuestionCommand.builder()
                .title("Title")
                .description("Description")
                .tag("tag")
                .build();
        authenticationFacade.register(register);

        // Ask a question and answer.
        var questionId = questionFacade.askQuestion(question);
        var answer = AnswerQuestionCommand.builder()
                .question(questionId)
                .body("This is a stupid question")
                .tag("tag")
                .build();
        var answerId = assertDoesNotThrow(() -> answerFacade.answer(answer));

            // Make sure the question was added.
            var answers = answerFacade.getAnswers(AnswerQuery.builder()
                    .tag("tag")
                    .forQuestion(questionId)
                    .build())
                    .getAnswers();
            assertEquals(1, answers.size());

            // select the answer.
            assertDoesNotThrow(() -> answerFacade.select(SelectAnswerCommand.builder()
                            .forQuestion(questionId)
                            .answer(answerId)
                            .tag("tag")
                            .build()));
    }

    @Test
    public void testNonQuestionOwnerCanNotSelectAnswerAsAccepted() {

        final String tagAlice = "tagAlice";
        final String tagBob = "tagBob";

        // Register.
        var registerAlice = RegisterCommand.builder()
                .username("alice")
                .password("password1")
                .tag(tagAlice)
                .build();

        var registerBob = RegisterCommand.builder()
                .username("bob")
                .password("password2")
                .tag(tagAlice)
                .build();

        var question = AskQuestionCommand.builder()
                .title("Title")
                .description("Description")
                .tag(tagAlice)
                .build();
        authenticationFacade.register(registerAlice);
        authenticationFacade.register(registerBob);

        // Ask a question and answer.
        var questionId = questionFacade.askQuestion(question);
        var answer = AnswerQuestionCommand.builder()
                .question(questionId)
                .body("This is a stupid question")
                .tag(tagAlice)
                .build();
        var answerId = assertDoesNotThrow(() -> answerFacade.answer(answer));

            // Make sure the question was added.
            var answers = answerFacade.getAnswers(AnswerQuery.builder()
                    .tag(tagAlice)
                    .forQuestion(questionId)
                    .build())
                    .getAnswers();
            assertEquals(1, answers.size());


            // TODO: This test is behaving weirdly regarding code coverage in AnswerFacade.select (throw new AuthenticationFailedException())
            // try to select the answer.
            assertThrows(AuthenticationFailedException.class, () ->
                    answerFacade.select(SelectAnswerCommand.builder()
                    .forQuestion(questionId)
                    .answer(answerId)
                    .tag(tagBob)
                    .build()));
    }

    @Test
    public void testQuestionOwnerCanUnselectAcceptedAnswer() {

        // Register.
        var register = RegisterCommand.builder()
                .username("alice")
                .password("password")
                .tag("tag")
                .build();
        var question = AskQuestionCommand.builder()
                .title("Title")
                .description("Description")
                .tag("tag")
                .build();
        authenticationFacade.register(register);

        // Ask a question and answer.
        var questionId = questionFacade.askQuestion(question);
        var answer = AnswerQuestionCommand.builder()
                .question(questionId)
                .body("This is a stupid question")
                .tag("tag")
                .build();
        var answerId = assertDoesNotThrow(() -> answerFacade.answer(answer));

        // Make sure the question was added.
        var answersBefore = answerFacade.getAnswers(AnswerQuery.builder()
                .tag("tag")
                .forQuestion(questionId)
                .build())
                .getAnswers();
        assertEquals(1, answersBefore.size());

        // select the answer.
        assertDoesNotThrow(() -> answerFacade.select(SelectAnswerCommand.builder()
                .forQuestion(questionId)
                .answer(answerId)
                .tag("tag")
                .build()));

        // unselect the answer.
        assertDoesNotThrow(() -> answerFacade.unselect(UnselectAnswerCommand.builder()
                .forQuestion(questionId)
                .tag("tag")
                .build()));

        // verify answer is not selected
        var answersAfter = answerFacade.getAnswers(AnswerQuery.builder()
                .tag("tag")
                .forQuestion(questionId)
                .build())
                .getAnswers();

        assertEquals(answersAfter.size(), 1);

        assertEquals(answersAfter.get(0).isSelected(), false);
    }

    @Test
    public void testNonQuestionOwnerCanNotUnselectAnswer() {

        final String tagAlice = "tagAlice";
        final String tagBob = "tagBob";

        // Register.
        var registerAlice = RegisterCommand.builder()
                .username("alice")
                .password("password1")
                .tag(tagAlice)
                .build();

        var registerBob = RegisterCommand.builder()
                .username("bob")
                .password("password2")
                .tag(tagAlice)
                .build();

        var question = AskQuestionCommand.builder()
                .title("Title")
                .description("Description")
                .tag(tagAlice)
                .build();
        authenticationFacade.register(registerAlice);
        authenticationFacade.register(registerBob);

        // Ask a question and answer.
        var questionId = questionFacade.askQuestion(question);
        var answer = AnswerQuestionCommand.builder()
                .question(questionId)
                .body("This is a stupid question")
                .tag(tagAlice)
                .build();
        var answerId = assertDoesNotThrow(() -> answerFacade.answer(answer));

        // Make sure the question was added.
        var answers = answerFacade.getAnswers(AnswerQuery.builder()
                .tag(tagAlice)
                .forQuestion(questionId)
                .build())
                .getAnswers();
        assertEquals(1, answers.size());


        // TODO: This test is behaving weirdly regarding code coverage in AnswerFacade.unselect (throw new AuthenticationFailedException())
        // select the answer.
        assertDoesNotThrow(() ->
                answerFacade.select(SelectAnswerCommand.builder()
                        .forQuestion(questionId)
                        .answer(answerId)
                        .tag(tagAlice)
                        .build()));

        // try to unselect the answer as bob.
        assertThrows(AuthenticationFailedException.class, () -> answerFacade.unselect(UnselectAnswerCommand.builder()
                .forQuestion(questionId)
                .tag(tagBob)
                .build()));
    }
}

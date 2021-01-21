package ch.heigvd.amt.stack.application;

import ch.heigvd.amt.stack.application.answer.command.*;
import ch.heigvd.amt.stack.application.answer.dto.AnswerDTO;
import ch.heigvd.amt.stack.application.answer.query.AnswerQuery;
import ch.heigvd.amt.stack.application.authentication.command.RegisterCommand;
import ch.heigvd.amt.stack.application.authentication.command.UnregisterCommand;
import ch.heigvd.amt.stack.application.question.command.AskQuestionCommand;
import ch.heigvd.amt.stack.application.question.command.DeleteQuestionCommand;
import ch.heigvd.amt.stack.domain.answer.Answer;
import ch.heigvd.amt.stack.domain.answer.AnswerId;
import ch.heigvd.amt.stack.domain.answer.AnswerNotFoundException;
import ch.heigvd.amt.stack.domain.comment.CommentId;
import ch.heigvd.amt.stack.domain.comment.CommentNotFoundException;
import ch.heigvd.amt.stack.domain.question.QuestionNotFoundException;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.List;

@RunWith(Arquillian.class)
public class AnswerFacadeIT {

    @Inject
    AuthenticationFacade authenticationFacade;
    @Inject
    QuestionFacade questionFacade;
    @Inject
    AnswerFacade answerFacade;

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "arquillian-managed.war")
                .addPackages(true, "ch.heigvd", "org.mindrot.jbcrypt");
    }

    private void register() {
        authenticationFacade.register(RegisterCommand.builder()
                .username("ciri")
                .password("lioncub")
                .tag("zirael")
                .build());

        authenticationFacade.register(RegisterCommand.builder()
                .username("geralt")
                .password("gwynbleidd")
                .tag("witcher")
                .build());

        authenticationFacade.register(RegisterCommand.builder()
                .username("yennefer")
                .password("vengerberg")
                .tag("magic")
                .build());
    }


    @Test
    public void testAnUserCanAnswerAQuestionOfOtherUserAndDeleteIt() {
        register();
        var id = questionFacade.askQuestion(AskQuestionCommand.builder()
                .title("Do you dream of the Wild Hunt ?")
                .description("They are so lovely...")
                .tag("zirael").build());

        AnswerId idA1 = null;
        try {
            idA1 = (answerFacade.answer(AnswerQuestionCommand.builder()
                    .question(id)
                    .body("No, 'cause I killed their king.")
                    .tag("witcher").build()));
        } catch (QuestionNotFoundException e) {
            Assert.fail();
        }
        Assert.assertNotNull(idA1);

        try {
            answerFacade.delete(DeleteAnswerCommand.builder()
                    .answer(idA1)
                    .tag("witcher").build());
        } catch (AnswerNotFoundException e) {
            Assert.fail();
        }
        Assert.assertEquals(0, answerFacade.getAnswers(AnswerQuery.builder()
                .forQuestion(id)
                .tag("zirael").build()).getAnswers().size());

        unregister();
    }

    @Test
    public void testDeleteAQuestionDeletesAnswers() {
        register();
        var id = questionFacade.askQuestion(AskQuestionCommand.builder()
                .title("Do you dream of the Wild Hunt ?")
                .description("They are so lovely...")
                .tag("zirael").build());

        AnswerId idA1 = null, idA2 = null;
        try {
            idA1 = answerFacade.answer(AnswerQuestionCommand.builder()
                    .question(id)
                    .body("No, 'cause I killed their king.")
                    .tag("witcher").build());
            idA2 = answerFacade.answer(AnswerQuestionCommand.builder()
                    .question(id)
                    .body("No, I've got the magic")
                    .tag("magic").build());
        } catch (QuestionNotFoundException e) {
            Assert.fail();
        }
        Assert.assertNotNull(idA1);
        Assert.assertNotNull(idA2);
        //2 answers expected
        Assert.assertEquals(2, answerFacade.getAnswers(AnswerQuery.builder()
                .forQuestion(id)
                .tag("zirael").build()).getAnswers().size());

        try {
            questionFacade.deleteQuestion(DeleteQuestionCommand.builder()
                    .question(id)
                    .tag("zirael").build());
        } catch (Exception e) {
            Assert.fail();
        }

        Assert.assertEquals(0, answerFacade.getAnswers(AnswerQuery.builder()
                .forQuestion(id)
                .tag("zirael").build()).getAnswers().size());

        unregister();
    }

    @Test
    public void testAnUserCanCommentAndDeleteComments() {
        register();

        var id = questionFacade.askQuestion(AskQuestionCommand.builder()
                .title("What is really magic ?")
                .description("Excluding blue lightnings")
                .tag("zirael").build());

        AnswerId idA2 = null;
        try {
            idA2 = answerFacade.answer(AnswerQuestionCommand.builder()
                    .question(id)
                    .body("It's a curse, a blessing and a progression")
                    .tag("magic").build());
        } catch (QuestionNotFoundException e) {
            Assert.fail();
        }
        Assert.assertNotNull(idA2);

        CommentId idC1 = null;
        try {
            idC1 = answerFacade.comment(CommentAnswerCommand.builder()
                    .answer(idA2)
                    .body("I knew this, Yenna...")
                    .tag("zirael").build());
        } catch (AnswerNotFoundException e) {
            Assert.fail();
        }
        Assert.assertNotNull(idC1);

        Assert.assertEquals(1, answerFacade.getAnswers(AnswerQuery.builder()
                .forQuestion(id)
                .tag("zirael").build()).getAnswers().get(0).getComments().size());

        //AssertDoesNotThrow would be very useful here...
        try {
            answerFacade.deleteComment(DeleteCommentCommand.builder()
                    .comment(idC1)
                    .tag("zirael").build());
        } catch (Exception e) {
            Assert.fail();
        }

        Assert.assertEquals(0, answerFacade.getAnswers(AnswerQuery.builder()
                .forQuestion(id)
                .tag("zirael").build()).getAnswers().get(0).getComments().size());

        unregister();
    }

    @Test
    public void testAnUserCanUpvoteAndDownvote() {
        register();
        var id = questionFacade.askQuestion(AskQuestionCommand.builder()
                .title("What is really magic ?")
                .description("Excluding blue lightnings")
                .tag("zirael").build());

        AnswerId idA2 = null;
        try {
            idA2 = answerFacade.answer(AnswerQuestionCommand.builder()
                    .question(id)
                    .body("It's a curse, a blessing and a progression")
                    .tag("magic").build());
        } catch (QuestionNotFoundException e) {
            Assert.fail();
        }
        Assert.assertNotNull(idA2);

        answerFacade.upvote(UpvoteAnswerCommand.builder()
                .answer(idA2)
                .tag("zirael").build());

        Assert.assertEquals(1, answerFacade.getAnswers(AnswerQuery.builder()
                .forQuestion(id)
                .tag("zirael").build()).getAnswers().get(0).getPositiveVotesCount());
        Assert.assertEquals(0, answerFacade.getAnswers(AnswerQuery.builder()
                .forQuestion(id)
                .tag("zirael").build()).getAnswers().get(0).getNegativeVotesCount());

        answerFacade.downvote(DownvoteAnswerCommand.builder()
                .answer(idA2)
                .tag("zirael").build());

        Assert.assertEquals(0, answerFacade.getAnswers(AnswerQuery.builder()
                .forQuestion(id)
                .tag("zirael").build()).getAnswers().get(0).getPositiveVotesCount());
        Assert.assertEquals(1, answerFacade.getAnswers(AnswerQuery.builder()
                .forQuestion(id)
                .tag("zirael").build()).getAnswers().get(0).getNegativeVotesCount());

        unregister();
    }

    private void unregister() {
        authenticationFacade.unregister(UnregisterCommand.builder()
                .username("ciri")
                .password("lioncub")
                .build());

        authenticationFacade.unregister(UnregisterCommand.builder()
                .username("geralt")
                .password("gwynbleidd")
                .build());

        authenticationFacade.unregister(UnregisterCommand.builder()
                .username("yennefer")
                .password("vengerberg")
                .build());
    }
}

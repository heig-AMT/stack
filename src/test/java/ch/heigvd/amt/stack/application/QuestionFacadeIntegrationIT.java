package ch.heigvd.amt.stack.application;

import ch.heigvd.amt.stack.application.authentication.command.RegisterCommand;
import ch.heigvd.amt.stack.application.authentication.command.UnregisterCommand;
import ch.heigvd.amt.stack.application.question.command.AskQuestionCommand;
import ch.heigvd.amt.stack.application.question.command.DeleteQuestionCommand;
import ch.heigvd.amt.stack.application.question.dto.QuestionDTO;
import ch.heigvd.amt.stack.application.question.query.QuestionQuery;
import ch.heigvd.amt.stack.domain.authentication.AuthenticationFailedException;
import ch.heigvd.amt.stack.domain.question.Question;
import ch.heigvd.amt.stack.domain.question.QuestionNotFoundException;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class QuestionFacadeIntegrationIT {

    @Inject
    AuthenticationFacade authenticationFacade;

    @Inject
    QuestionFacade questionFacade;

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "arquillian-managed.war")
                .addPackages(true, "ch.heigvd.amt", "org.mindrot.jbcrypt");
    }

    @Test
    public void oneUserRegisteredCanAskAndDeleteQuestion() {
        authenticationFacade.register(RegisterCommand.builder()
                .username("ciri")
                .password("lioncub")
                .tag("zirael")
                .build());

        questionFacade.askQuestion(AskQuestionCommand.builder()
                .title("Do you dream of Scorchers ?")
                .description("They are so lovely...")
                .tag("zirael").build());

        List<QuestionDTO> questions = questionFacade.getQuestions(QuestionQuery.builder()
                .shouldContain("Do you dream of Scorchers ?").build())
                .getQuestions();

        assert (questions.size() == 1);

        //AssertDoesNoThrow seems not exist anymore...
        try {
            questionFacade.deleteQuestion(DeleteQuestionCommand.builder()
                    .question(
                            questions.get(0).getId()
                    )
                    .tag("zirael")
                    .build());
        } catch (QuestionNotFoundException e) {
            e.printStackTrace();
        }
        questions = questionFacade.getQuestions(QuestionQuery.builder()
                .shouldContain("Do you dream of Scorchers ?").build())
                .getQuestions();

        assert (questions.size() == 0);

        authenticationFacade.unregister(UnregisterCommand.builder()
                .username("ciri")
                .password("lioncub")
                .build());
    }
}

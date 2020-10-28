package ch.heigvd.amt.stack.application;

import ch.heigvd.amt.stack.application.authentication.command.LoginCommand;
import ch.heigvd.amt.stack.application.authentication.command.LogoutCommand;
import ch.heigvd.amt.stack.application.authentication.command.RegisterCommand;
import ch.heigvd.amt.stack.application.authentication.dto.ConnectedDTO;
import ch.heigvd.amt.stack.application.authentication.query.SessionQuery;
import lombok.extern.java.Log;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.assertThrows;
//import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
public class AuthenticationFacadeIT {

    @Inject
    AuthenticationFacade authenticationFacade;

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "arquillian-managed.war")
                .addPackages(true, "ch.heigvd.amt", "org.mindrot.jbcrypt");
    }

    @Test
    public void testConnection() {
        Assert.assertNotNull(authenticationFacade);

        try {
            authenticationFacade.register(RegisterCommand.builder()
                    .username("alice")
                    .password("password")
                    .tag("tag")
                    .build());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        authenticationFacade.login(LoginCommand.builder()
                .username("alice")
                .password("password")
                .tag("tag")
                .build());

        var connected = authenticationFacade.connected(SessionQuery.builder()
                .tag("tag")
                .build());

        Assert.assertTrue(connected.isConnected());
    }

    @Test
    public void testTwoDifferentUsersCanSignUp() {
        Assert.assertNotNull(authenticationFacade);

        authenticationFacade.register(RegisterCommand.builder()
                .username("aloy")
                .password("chieftain")
                .tag("scorcher")
                .build());
        ConnectedDTO connectedFirstUser = authenticationFacade.connected(SessionQuery.builder().tag("scorcher").build());

        authenticationFacade.register(RegisterCommand.builder()
                .username("ikrie")
                .password("banuk")
                .tag("scrapper")
                .build());
        ConnectedDTO connectedSecondUser = authenticationFacade.connected(SessionQuery.builder().tag("scrapper").build());

        Assert.assertTrue(connectedFirstUser.isConnected());
        Assert.assertTrue(connectedSecondUser.isConnected());
    }

    /*@Test
    public void testOneUserCanNotSignUpTwoTimes() {


        authenticationFacade.register(RegisterCommand.builder()
                .username("aloy")
                .password("chieftain")
                .tag("scorcher")
                .build());

        assertThrows(AuthenticationFailedException.class, () -> {
            authenticationFacade.register(RegisterCommand.builder()
                    .username("aloy")
                    .password("chieftain")
                    .tag("scorcher")
                    .build());
        });

    }

    /*@Test
    public void oneUserCanRegisterThenLogin() {
        authenticationFacade.register(RegisterCommand.builder()
                .username("aloy")
                .password("chieftain")
                .tag("scorcher")
                .build());

        authenticationFacade.login(LoginCommand.builder()
                .username("aloy")
                .password("chieftain")
                .tag("scorcher")
                .build());
        ConnectedDTO connectedLogin = authenticationFacade.connected(SessionQuery.builder().tag("scorcher").build());

        assertTrue(connectedLogin.isConnected());

    }

    @Test
    public void oneUserWhoDoesNotExistCannotLogin() {

        assertThrows(AuthenticationFailedException.class, () -> {
            authenticationFacade.login(LoginCommand.builder()
                    .username("aloy")
                    .password("chieftain")
                    .tag("scorcher")
                    .build());
        });

    }

    @Test
    public void oneUserCanRegisterThenLogout() {


        authenticationFacade.register(RegisterCommand.builder()
                .username("aloy")
                .password("chieftain")
                .tag("scorcher")
                .build());

        authenticationFacade.logout(LogoutCommand.builder()
                .tag("scorcher")
                .build());
        ConnectedDTO connectedLogin = authenticationFacade.connected(SessionQuery.builder().tag("scorcher").build());

        assertTrue(connectedLogin.isConnected());
    }*/
}

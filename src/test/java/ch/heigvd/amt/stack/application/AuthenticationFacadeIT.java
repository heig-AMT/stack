package ch.heigvd.amt.stack.application;

import ch.heigvd.amt.stack.application.authentication.command.LoginCommand;
import ch.heigvd.amt.stack.application.authentication.command.LogoutCommand;
import ch.heigvd.amt.stack.application.authentication.command.RegisterCommand;
import ch.heigvd.amt.stack.application.authentication.dto.ConnectedDTO;
import ch.heigvd.amt.stack.application.authentication.query.SessionQuery;
import ch.heigvd.amt.stack.domain.authentication.AuthenticationFailedException;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class AuthenticationFacadeIT {

    @Inject
    AuthenticationFacade authenticationFacade;

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "arquillian-managed.war")
                .addPackages(true, "ch.heigvd", "org.mindrot.jbcrypt")
                .addPackages(true, "okhttp3");
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

        assertTrue(connected.isConnected());
    }

    @Test
    public void testTwoDifferentUsersCanSignUp() {
        authenticationFacade.register(RegisterCommand.builder()
                .username("talanah")
                .password("sunhawk")
                .tag("redmaw")
                .build());
        ConnectedDTO connectedFirstUser = authenticationFacade.connected(SessionQuery.builder().tag("redmaw").build());

        authenticationFacade.register(RegisterCommand.builder()
                .username("ikrie")
                .password("banuk")
                .tag("scrapper")
                .build());
        ConnectedDTO connectedSecondUser = authenticationFacade.connected(SessionQuery.builder().tag("scrapper").build());

        assertTrue(connectedFirstUser.isConnected());
        assertTrue(connectedSecondUser.isConnected());
    }

    @Test
    public void testOneUserCanNotSignUpTwoTimes() {
        authenticationFacade.register(RegisterCommand.builder()
                .username("varga")
                .password("oseram")
                .tag("icerail")
                .build());

        assertThrows(AuthenticationFailedException.class, () -> {
            authenticationFacade.register(RegisterCommand.builder()
                    .username("varga")
                    .password("oseram")
                    .tag("icerail")
                    .build());
        });
    }

    @Test
    public void testOneUserCanRegisterAndLoginAndLogout() {
        authenticationFacade.register(RegisterCommand.builder()
                .username("aloy")
                .password("chieftain")
                .tag("scorcher")
                .build());
        ConnectedDTO connectedAuth = authenticationFacade.connected(SessionQuery.builder().tag("scorcher").build());

        authenticationFacade.login(LoginCommand.builder()
                .username("aloy")
                .password("chieftain")
                .tag("scorcher")
                .build());
        ConnectedDTO connectedLogin = authenticationFacade.connected(SessionQuery.builder().tag("scorcher").build());

        authenticationFacade.logout(LogoutCommand.builder()
                .tag("scorcher")
                .build());
        ConnectedDTO connectedLogout = authenticationFacade.connected(SessionQuery.builder().tag("scorcher").build());

        assertTrue(connectedAuth.isConnected());
        assertTrue(connectedLogin.isConnected());
        assertFalse(connectedLogout.isConnected());
    }

    @Test
    public void oneUserWhoDoesNotExistCannotLogin() {
        assertThrows(AuthenticationFailedException.class, () -> {
            authenticationFacade.login(LoginCommand.builder()
                    .username("varl")
                    .password("nora")
                    .tag("hunter")
                    .build());
        });
    }
}

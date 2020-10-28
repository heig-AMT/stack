package ch.heigvd.amt.stack.application;

import ch.heigvd.amt.stack.application.authentication.command.ChangePasswordCommand;
import ch.heigvd.amt.stack.application.authentication.command.LoginCommand;
import ch.heigvd.amt.stack.application.authentication.command.LogoutCommand;
import ch.heigvd.amt.stack.application.authentication.command.RegisterCommand;
import ch.heigvd.amt.stack.application.authentication.query.SessionQuery;
import ch.heigvd.amt.stack.domain.authentication.AuthenticationFailedException;
import ch.heigvd.amt.stack.infrastructure.persistence.memory.InMemoryCredentialRepository;
import ch.heigvd.amt.stack.infrastructure.persistence.memory.InMemorySessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthenticationFacadeIntegration {

    private AuthenticationFacade facade;

    @BeforeEach
    public void prepare() {
        // TODO : Switch to a DI mechanism.
        var credentials = new InMemoryCredentialRepository();
        var sessions = new InMemorySessionRepository();

        facade = new AuthenticationFacade();
        facade.credentials = credentials;
        facade.sessions = sessions;
    }

    @Test
    public void testNonExistingUserCanNotLogin() {
        var command = LoginCommand.builder()
                .username("alice")
                .password("password")
                .tag("tag")
                .build();

        assertThrows(AuthenticationFailedException.class, () -> {
            facade.login(command);
        });
    }

    @Test
    public void testNonExistingUserCanLogOut() {
        var command = LogoutCommand.builder()
                .tag("tag")
                .build();

        assertDoesNotThrow(() -> {
            facade.logout(command);
        });
    }

    @Test
    public void testNonExistingUserCanRegister() {
        var command = RegisterCommand.builder()
                .username("alice")
                .password("password")
                .tag("tag")
                .build();

        assertDoesNotThrow(() -> {
            facade.register(command);
        });
    }

    @Test
    public void testRegisteringUserCanLogin() {
        var register = RegisterCommand.builder()
                .username("alice")
                .password("password")
                .tag("tag1")
                .build();
        var login = LoginCommand.builder()
                .username("alice")
                .password("password")
                .tag("tag2")
                .build();
        facade.register(register);
        assertDoesNotThrow(() -> {
            facade.login(login);
        });
    }

    @Test
    public void testRegisteredUserWithSessionIsNowConnected() {
        var register = RegisterCommand.builder()
                .username("alice")
                .password("password")
                .tag("tag1")
                .build();
        var query = SessionQuery.builder()
                .tag("tag1")
                .build();
        facade.register(register);
        var connected = facade.connected(query);
        assertTrue(connected.isConnected());
        assertEquals("alice", connected.getUsername());
    }

    @Test
    public void testDefaultSessionIsNotConnected() {
        var query = SessionQuery.builder()
                .tag("tag")
                .build();
        var connected = facade.connected(query);
        assertFalse(connected.isConnected());
        assertNull(connected.getUsername());
    }

    @Test
    public void testAlreadyRegisteredUserCanNotRegisterAgain() {
        var register = RegisterCommand.builder()
                .username("alice")
                .password("bob")
                .tag("tag")
                .build();

        facade.register(register);
        assertThrows(AuthenticationFailedException.class, () -> {
            facade.register(register);
        });
    }

    @Test
    public void testRegisteredUserNeedsPasswordToLogin() {
        var register = RegisterCommand.builder()
                .username("alice")
                .password("bob")
                .tag("tag")
                .build();
        var login = LoginCommand.builder()
                .username("alice")
                .password("charlie")
                .tag("tag")
                .build();

        facade.register(register);
        assertThrows(AuthenticationFailedException.class, () -> {
            facade.login(login);
        });
    }

    @Test
    public void testRegisteredUserCanChangePasswordAndStayConnected() {
        var register = RegisterCommand.builder()
                .username("alice")
                .password("password")
                .tag("tag")
                .build();

        facade.register(register);

        var changePassword = ChangePasswordCommand.builder()
                .username("alice")
                .currentPassword("password")
                .newPassword("iloveyou")
                .build();

        facade.changePassword(changePassword);
        var connected = facade.connected(SessionQuery.builder().tag("tag").build());

        assertTrue(connected.isConnected());
        assertEquals("alice", connected.getUsername());
    }

    @Test
    public void testRegisteredUserCanNotChangePasswordWithIncorrectPassword() {
        var register = RegisterCommand.builder()
                .username("alice")
                .password("password")
                .tag("tag")
                .build();

        facade.register(register);

        var changePassword = ChangePasswordCommand.builder()
                .username("alice")
                .currentPassword("wrong")
                .newPassword("iloveyou")
                .build();

        assertThrows(AuthenticationFailedException.class, () -> {
            facade.changePassword(changePassword);
        });
    }

    @Test
    public void testCanNotChangePasswordForMissingUser() {
        var changePassword = ChangePasswordCommand.builder()
                .username("alice")
                .currentPassword("missing")
                .newPassword("iloveyou")
                .build();

        assertThrows(AuthenticationFailedException.class, () -> {
            facade.changePassword(changePassword);
        });
    }
}

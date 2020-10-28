package ch.heigvd.amt.stack.application;

import ch.heigvd.amt.stack.application.authentication.command.LoginCommand;
import ch.heigvd.amt.stack.application.authentication.command.LogoutCommand;
import ch.heigvd.amt.stack.application.authentication.command.RegisterCommand;
import ch.heigvd.amt.stack.application.authentication.dto.ConnectedDTO;
import ch.heigvd.amt.stack.application.authentication.query.CredentialQuery;
import ch.heigvd.amt.stack.application.authentication.query.SessionQuery;
import ch.heigvd.amt.stack.domain.authentication.*;
import org.mindrot.jbcrypt.BCrypt;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

@RequestScoped
public class AuthenticationFacade {

    @Inject
    CredentialRepository credentials;

    @Inject
    SessionRepository sessions;

    /**
     * Logs in an existing user to a certain session. If the credentials do not match, an exception will be thrown to
     * indicate that the operation failed.
     *
     * @param command The {@link LoginCommand} containing the login information for that specific user.
     * @throws AuthenticationFailedException If something goes bad during the login process.
     */
    public void login(LoginCommand command) throws AuthenticationFailedException {
        logout(LogoutCommand.builder().tag(command.getTag()).build());

        Credential credential = credentials.findBy(CredentialQuery.builder()
                .username(command.getUsername())
                .build())
                .orElseThrow(AuthenticationFailedException::new);

        if (!BCrypt.checkpw(command.getPassword(), credential.getHashedPassword())) {
            throw new AuthenticationFailedException();
        }

        sessions.save(Session.builder()
                .tag(command.getTag())
                .user(credential.getId())
                .build());
    }

    public void logout(LogoutCommand command) {
        sessions.findBy(SessionQuery.builder()
                .tag(command.getTag())
                .build())
                .ifPresent(s -> sessions.remove(s.getId()));
    }

    public void register(RegisterCommand command) throws AuthenticationFailedException {
        logout(LogoutCommand.builder().tag(command.getTag()).build());

        credentials.findBy(CredentialQuery.builder()
                .username(command.getUsername())
                .build())
                .ifPresent(credential -> {
                    throw new AuthenticationFailedException();
                });

        var id = CredentialId.create();

        credentials.save(Credential.builder()
                .id(id)
                .username(command.getUsername())
                .hashedPassword(BCrypt.hashpw(command.getPassword(), BCrypt.gensalt()))
                .build());

        sessions.save(Session.builder()
                .tag(command.getTag())
                .user(id)
                .build());
    }

    /**
     * Returns a {@link ConnectedDTO} that indicates whether a certain user is tagged with a certain value, and
     * therefore connected.
     *
     * @param query The {@link SessionQuery} that specifies the tag that we're looking for.
     * @return A {@link ConnectedDTO} with true set if the user is connected.
     */
    public ConnectedDTO connected(SessionQuery query) {
        boolean connected = sessions.findBy(query)
                .map(session -> session.getUser() != null)
                .orElse(false);
        return ConnectedDTO.builder()
                .connected(connected)
                .build();
    }
}

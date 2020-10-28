package ch.heigvd.amt.stack.application;

import ch.heigvd.amt.stack.application.authentication.command.LoginCommand;
import ch.heigvd.amt.stack.application.authentication.command.RegisterCommand;
import ch.heigvd.amt.stack.application.authentication.query.SessionQuery;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

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
}

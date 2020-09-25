package ch.heigvd.amt.stack.application.authentication.command;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class LoginCommand {
    String username;
    String password;
    String tag;
}

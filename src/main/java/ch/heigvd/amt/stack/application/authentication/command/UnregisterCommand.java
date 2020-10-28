package ch.heigvd.amt.stack.application.authentication.command;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UnregisterCommand {
    String username;
    String password;
}

package ch.heigvd.amt.stack.application.authentication.command;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class ChangePasswordCommand {
    String username;
    String currentPassword;
    String newPassword;
}

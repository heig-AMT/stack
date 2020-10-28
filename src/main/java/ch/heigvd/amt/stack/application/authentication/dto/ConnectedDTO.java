package ch.heigvd.amt.stack.application.authentication.dto;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class ConnectedDTO {
    boolean connected;
    String username;
}

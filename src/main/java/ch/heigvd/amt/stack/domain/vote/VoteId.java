package ch.heigvd.amt.stack.domain.vote;

import ch.heigvd.amt.stack.domain.Id;
import ch.heigvd.amt.stack.domain.answer.AnswerId;
import ch.heigvd.amt.stack.domain.authentication.CredentialId;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@Builder
@EqualsAndHashCode
public class VoteId implements Id {
    AnswerId answer;
    CredentialId voter;
}

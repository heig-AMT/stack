package ch.heigvd.amt.stack.domain.vote;

import ch.heigvd.amt.stack.domain.answer.AnswerId;
import ch.heigvd.amt.stack.domain.authentication.CredentialId;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class VoteId {
    AnswerId answer;
    CredentialId voter;
}

package ch.heigvd.amt.stack.domain.vote;

import ch.heigvd.amt.stack.domain.Entity;
import ch.heigvd.amt.stack.domain.answer.AnswerId;
import ch.heigvd.amt.stack.domain.authentication.CredentialId;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Vote {

   VoteId id;
   boolean upOrDown;

    public AnswerId getAnswer() {
        return id.answer;
    }

    public CredentialId getCredential() {
        return id.voter;
    }
}

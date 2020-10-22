package ch.heigvd.amt.stack.domain.vote;

import ch.heigvd.amt.stack.domain.Id;
import ch.heigvd.amt.stack.domain.answer.Answer;
import ch.heigvd.amt.stack.domain.answer.AnswerId;
import ch.heigvd.amt.stack.domain.authentication.CredentialId;

public class VoteId {
    AnswerId answer;
    CredentialId voter;

    public VoteId(AnswerId answer, CredentialId voter)
    {
        this.answer=answer;
        this.voter=voter;
    }

    public AnswerId getAnswer() {
        return answer;
    }

    public CredentialId getCredential() {
        return voter;
    }
}

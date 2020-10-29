package ch.heigvd.amt.stack.application.question.dto;

import ch.heigvd.amt.stack.domain.question.Question;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * An enumeration representing the different states that a question can be in. Depending on when the question was
 * asked, the status will be (in order of preference) :
 * <p>
 * - resolved if it has been answered; or
 * - new if it has been opened in the last two days; or
 * - open otherwise.
 */
public enum QuestionStatusDTO {
    Open,
    New,
    Resolved;

    /**
     * Returns the {@link QuestionStatusDTO} from a certain {@link Question} and the current {@link Instant}.
     *
     * @param question the question that we're looking into.
     * @param now      the current moment.
     * @return the appropriate {@link QuestionStatusDTO}.
     */
    public static QuestionStatusDTO from(Question question, Instant now) {
        if (question.getSelectedAnswer() != null) {
            return Resolved;
        } else if (now.minus(2, ChronoUnit.DAYS).isBefore(question.getCreation())) {
            return New;
        } else {
            return Open;
        }
    }
}

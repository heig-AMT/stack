package ch.heigvd.amt.stack.application.answer;

import ch.heigvd.amt.stack.application.answer.dto.AnswerDTO;
import ch.heigvd.amt.stack.application.answer.dto.AnswerListDTO;
import ch.heigvd.amt.stack.application.answer.query.AnswerQuery;

import javax.inject.Inject;
import java.time.Instant;
import java.util.List;

public class AnswerFacade {

    @Inject
    public AnswerFacade() {
    }

    public AnswerListDTO getAnswers(AnswerQuery query) {
        List<AnswerDTO> answers = List.of(
                AnswerDTO.builder()
                        .author("Alice")
                        .creation(Instant.now().minusSeconds(24 * 3600))
                        .body("This is an answer that's been given by Alice.")
                        .build(),
                AnswerDTO.builder()
                        .author("Bob")
                        .creation(Instant.now().minusSeconds(3600))
                        .body("This is an answer that's been given by Bob.")
                        .build(),
                AnswerDTO.builder()
                        .author("Charlie")
                        .creation(Instant.now())
                        .body("This is an answer that's been given by Charlie.")
                        .build()
        );
        return AnswerListDTO.builder()
                .answers(answers)
                .build();
    }
}

package ch.heigvd.amt.stack.application.answer.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class AnswerListDTO {
    List<AnswerDTO> answers;
}

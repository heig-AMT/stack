package ch.heigvd.amt.stack.application.question.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class QuestionListDTO {
    List<QuestionDTO> questions;
}

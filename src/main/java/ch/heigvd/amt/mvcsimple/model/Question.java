package ch.heigvd.amt.mvcsimple.model;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Question {
    String title;
    String description;
}

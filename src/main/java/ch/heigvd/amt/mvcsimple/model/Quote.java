package ch.heigvd.amt.mvcsimple.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Quote {

    private String author;
    private String citation;
}

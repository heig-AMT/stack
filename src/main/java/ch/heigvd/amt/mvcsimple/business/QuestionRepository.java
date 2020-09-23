package ch.heigvd.amt.mvcsimple.business;

import ch.heigvd.amt.mvcsimple.model.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionRepository {

    private static final List<Question> questions = new ArrayList<>();

    static {
        questions.add(Question.builder()
                .title("My first title")
                .description("My first description")
                .build());
        questions.add(Question.builder()
                .title("Something else")
                .description("Something even different.")
                .build());
    }

    public List<Question> all() {
        return Collections.unmodifiableList(questions);
    }
}

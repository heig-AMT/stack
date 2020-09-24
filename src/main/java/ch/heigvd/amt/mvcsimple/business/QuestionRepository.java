package ch.heigvd.amt.mvcsimple.business;

import ch.heigvd.amt.mvcsimple.model.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionRepository {

    private static final List<Question> questions = new ArrayList<>();

    static {
        questions.add(Question.builder()
                .title("Why doesn't this program run ?")
                .description("I made the following program : <html></html>, but it displays nothing. Is it expected ? I'm using Opera btw.")
                .author("admin")
                .build());
        questions.add(Question.builder()
                .title("Can we inject an EJB in a JSP ?")
                .description("I have a semester project at HEIG-VD and still have not fully understood how beans work.")
                .author("admin")
                .build());
    }

    public List<Question> all() {
        return Collections.unmodifiableList(questions);
    }
}

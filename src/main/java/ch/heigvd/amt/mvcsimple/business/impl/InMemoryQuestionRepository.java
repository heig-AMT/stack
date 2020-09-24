package ch.heigvd.amt.mvcsimple.business.impl;

import ch.heigvd.amt.mvcsimple.business.api.QuestionRepository;
import ch.heigvd.amt.mvcsimple.model.Question;

import javax.ejb.Singleton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Singleton
public class InMemoryQuestionRepository implements QuestionRepository {

    private final List<Question> questions = new ArrayList<>();

    public InMemoryQuestionRepository() {
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

    @Override
    public void insert(String author, String title, String description) {
        questions.add(Question.builder()
                .author(author)
                .title(title)
                .description(description)
                .build());
    }

    public List<Question> all() {
        return Collections.unmodifiableList(questions);
    }
}

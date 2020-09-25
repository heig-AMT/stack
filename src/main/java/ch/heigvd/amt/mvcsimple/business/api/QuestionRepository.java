package ch.heigvd.amt.mvcsimple.business.api;

import ch.heigvd.amt.mvcsimple.model.Question;

import java.util.List;

public interface QuestionRepository {

    void insert(String author, String title, String description);

    List<Question> all();
}

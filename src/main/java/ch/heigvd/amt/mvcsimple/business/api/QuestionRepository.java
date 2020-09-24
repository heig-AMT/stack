package ch.heigvd.amt.mvcsimple.business.api;

import ch.heigvd.amt.mvcsimple.model.Question;

import javax.ejb.Local;
import java.util.List;

@Local
public interface QuestionRepository {

    void insert(String author, String title, String description);

    List<Question> all();
}

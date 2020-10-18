package ch.heigvd.amt.stack.domain.answer;

import ch.heigvd.amt.stack.application.question.query.QuestionQuery;
import ch.heigvd.amt.stack.domain.Repository;
import ch.heigvd.amt.stack.domain.question.Question;
import ch.heigvd.amt.stack.domain.question.QuestionId;

import java.util.Collection;

public interface AnswerRepository extends Repository<Answer, AnswerId>
{
}

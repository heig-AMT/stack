package ch.heigvd.amt.stack.application.question;

import ch.heigvd.amt.stack.application.question.dto.QuestionDTO;
import ch.heigvd.amt.stack.application.question.dto.QuestionListDTO;
import ch.heigvd.amt.stack.domain.question.Question;
import ch.heigvd.amt.stack.domain.question.QuestionRepository;

import java.util.List;
import java.util.stream.Collectors;

public class QuestionFacade {

    private final QuestionRepository repository;

    public QuestionFacade(QuestionRepository repository) {
        this.repository = repository;
    }

    public void askQuestion(AskQuestionCommand command) {
        repository.save(Question.builder()
                .author(command.getAuthor())
                .title(command.getTitle())
                .description(command.getDescription())
                .build()
        );
    }

    public QuestionListDTO getQuestions(QuestionQuery query) {
        List<QuestionDTO> questions =repository.findBy(query).stream()
                .map(question -> QuestionDTO.builder()
                        .author(question.getAuthor())
                        .title(question.getTitle())
                        .description(question.getDescription())
                        .build())
                .collect(Collectors.toUnmodifiableList());
        return QuestionListDTO.builder()
                .questions(questions)
                .build();
    }
}

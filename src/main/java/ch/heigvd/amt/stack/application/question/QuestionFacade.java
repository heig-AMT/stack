package ch.heigvd.amt.stack.application.question;

import ch.heigvd.amt.stack.application.authentication.query.SessionQuery;
import ch.heigvd.amt.stack.application.question.command.AskQuestionCommand;
import ch.heigvd.amt.stack.application.question.dto.QuestionDTO;
import ch.heigvd.amt.stack.application.question.dto.QuestionListDTO;
import ch.heigvd.amt.stack.application.question.dto.QuestionStatusDTO;
import ch.heigvd.amt.stack.application.question.query.QuestionQuery;
import ch.heigvd.amt.stack.application.question.query.SingleAnswerQuery;
import ch.heigvd.amt.stack.application.question.query.SingleQuestionQuery;
import ch.heigvd.amt.stack.domain.answer.Answer;
import ch.heigvd.amt.stack.domain.answer.AnswerRepository;
import ch.heigvd.amt.stack.domain.authentication.*;
import ch.heigvd.amt.stack.domain.question.Question;
import ch.heigvd.amt.stack.domain.question.QuestionId;
import ch.heigvd.amt.stack.domain.question.QuestionRepository;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequestScoped
public class QuestionFacade {

    private final AnswerRepository answerRepository;
    private final CredentialRepository credentialRepository;
    private final QuestionRepository repository;
    private final SessionRepository sessionRepository;
    private final Function<Question, QuestionDTO> questionToDto = new Function<>() {
        @Override
        public QuestionDTO apply(Question question) {
            return QuestionDTO.builder()
                    .author(credentialRepository.findById(question.getAuthor()).map(Credential::getUsername).get())
                    .title(question.getTitle())
                    .description(question.getDescription())
                    .creation(question.getCreation())
                    .status(QuestionStatusDTO.from(question, Instant.now()))
                    .id(question.getId())
                    .build();
        }
    };

    @Inject
    public QuestionFacade(
            AnswerRepository answerRepository,
            CredentialRepository credentialRepository,
            QuestionRepository repository,
            SessionRepository sessionRepository
    ) {
        this.answerRepository = answerRepository;
        this.credentialRepository = credentialRepository;
        this.repository = repository;
        this.sessionRepository = sessionRepository;
    }

    public QuestionId askQuestion(AskQuestionCommand command) throws AuthenticationFailedException {
        Session session = sessionRepository.findBy(SessionQuery.builder()
                .tag(command.getTag())
                .build())
                .orElseThrow(AuthenticationFailedException::new);
        var id = QuestionId.create();
        repository.save(Question.builder()
                .id(id)
                .author(session.getUser())
                .title(command.getTitle())
                .description(command.getDescription())
                .creation(Instant.now())
                .build()
        );
        return id;
    }

    public Optional<QuestionDTO> getQuestion(SingleQuestionQuery query) {
        return repository.findById(query.getId())
                .map(questionToDto);
    }

    public Optional<QuestionDTO> getQuestion(SingleAnswerQuery query) {
        return answerRepository.findById(query.getId()).stream()
                .map(Answer::getQuestion)
                .flatMap(id -> repository.findById(id).stream())
                .map(questionToDto)
                .findFirst();
    }

    public QuestionListDTO getQuestions(QuestionQuery query) {
        List<QuestionDTO> questions = repository.findBy(query).stream()
                .map(questionToDto)
                .collect(Collectors.toUnmodifiableList());
        return QuestionListDTO.builder()
                .questions(questions)
                .build();
    }
}

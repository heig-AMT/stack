package ch.heigvd.amt.stack.application;

import ch.heigvd.amt.stack.application.authentication.query.SessionQuery;
import ch.heigvd.amt.stack.application.question.command.AskQuestionCommand;
import ch.heigvd.amt.stack.application.question.command.DeleteQuestionCommand;
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
import ch.heigvd.amt.stack.domain.question.QuestionNotFoundException;
import ch.heigvd.amt.stack.domain.question.QuestionRepository;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@RequestScoped
public class QuestionFacade {

    @Inject
    AnswerRepository answerRepository;
    @Inject
    CredentialRepository credentialRepository;
    @Inject
    QuestionRepository repository;
    @Inject
    SessionRepository sessionRepository;

    private final BiFunction<CredentialId, Question, QuestionDTO> questionToDto = new BiFunction<>() {
        @Override
        public QuestionDTO apply(CredentialId user, Question question) {
            return QuestionDTO.builder()
                    .author(credentialRepository.findById(question.getAuthor()).map(Credential::getUsername).get())
                    .title(question.getTitle())
                    .description(question.getDescription())
                    .creation(question.getCreation())
                    .status(QuestionStatusDTO.from(question, Instant.now()))
                    .id(question.getId())
                    .deletionEnabled(Objects.equals(question.getAuthor(), user))
                    .build();
        }
    };

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

    public void deleteQuestion(DeleteQuestionCommand command) throws AuthenticationFailedException, QuestionNotFoundException {

        Session session = sessionRepository.findBy(SessionQuery.builder()
                .tag(command.getTag())
                .build())
                .orElseThrow(AuthenticationFailedException::new);

        var question = repository.findById(command.getQuestion())
                .orElseThrow(QuestionNotFoundException::new);

        if (!session.getUser().equals(question.getAuthor())) {
            throw new AuthenticationFailedException();
        }

        repository.remove(command.getQuestion());
    }

    private CredentialId getCredential(String forTag) {
        return sessionRepository.findBy(SessionQuery.builder()
                .tag(forTag)
                .build())
                .map(Session::getUser)
                .orElse(null);
    }

    public Optional<QuestionDTO> getQuestion(SingleQuestionQuery query) {
        return repository.findById(query.getId())
                .map(q -> questionToDto.apply(getCredential(query.getTag()), q));
    }

    public Optional<QuestionDTO> getQuestion(SingleAnswerQuery query) {
        return answerRepository.findById(query.getId()).stream()
                .map(Answer::getQuestion)
                .flatMap(id -> repository.findById(id).stream())
                .map(q -> questionToDto.apply(getCredential(query.getTag()), q))
                .findFirst();
    }

    public QuestionListDTO getQuestions(QuestionQuery query) {
        List<QuestionDTO> questions = repository.findBy(query).stream()
                .map(q -> questionToDto.apply(getCredential(query.getTag()), q))
                .sorted(Comparator.comparing(QuestionDTO::getCreation).reversed())
                .collect(Collectors.toUnmodifiableList());
        return QuestionListDTO.builder()
                .questions(questions)
                .build();
    }
}

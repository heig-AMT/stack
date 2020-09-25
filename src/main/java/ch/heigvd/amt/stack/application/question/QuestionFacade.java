package ch.heigvd.amt.stack.application.question;

import ch.heigvd.amt.stack.application.authentication.query.SessionQuery;
import ch.heigvd.amt.stack.application.question.command.AskQuestionCommand;
import ch.heigvd.amt.stack.application.question.dto.QuestionDTO;
import ch.heigvd.amt.stack.application.question.dto.QuestionListDTO;
import ch.heigvd.amt.stack.application.question.query.QuestionQuery;
import ch.heigvd.amt.stack.domain.authentication.*;
import ch.heigvd.amt.stack.domain.question.Question;
import ch.heigvd.amt.stack.domain.question.QuestionRepository;

import java.util.List;
import java.util.stream.Collectors;

public class QuestionFacade {

    private final CredentialRepository credentialRepository;
    private final QuestionRepository repository;
    private final SessionRepository sessionRepository;

    public QuestionFacade(
            CredentialRepository credentialRepository,
            QuestionRepository repository,
            SessionRepository sessionRepository
    ) {
        this.credentialRepository = credentialRepository;
        this.repository = repository;
        this.sessionRepository = sessionRepository;
    }

    public void askQuestion(AskQuestionCommand command) throws AuthenticationFailedException {
        Session session = sessionRepository.findBy(SessionQuery.builder()
                .tag(command.getTag())
                .build())
                .orElseThrow(AuthenticationFailedException::new);
        repository.save(Question.builder()
                .author(session.getUser())
                .title(command.getTitle())
                .description(command.getDescription())
                .build()
        );
    }

    public QuestionListDTO getQuestions(QuestionQuery query) {
        List<QuestionDTO> questions = repository.findBy(query).stream()
                .map(question -> QuestionDTO.builder()
                        .author(credentialRepository.findById(question.getAuthor()).map(Credential::getUsername).get())
                        .title(question.getTitle())
                        .description(question.getDescription())
                        .build())
                .collect(Collectors.toUnmodifiableList());
        return QuestionListDTO.builder()
                .questions(questions)
                .build();
    }
}

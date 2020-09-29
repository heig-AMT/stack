Feature('addQuestions');

const { I, registerPage, loginPage, questionsPage } = inject();

Scenario('Add a question', (I, registerPage, loginPage, questionsPage) => {
  const register = registerPage.register();

  loginPage.login(register.email, register.password);

  const question = questionsPage.addQuestion();

  I.see(question.title);
  I.see(question.description);
});

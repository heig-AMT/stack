Feature('questionList');

const { I, registerPage, loginPage, questionsPage, logoutPage } = inject();

Scenario('See questions when not logged in', (I, registerPage, logoutPage, questionsPage) => {
  registerPage.register();

  const question1 = questionsPage.addQuestion();
  logoutPage.logout();

  I.amOnPage('/questions');
  I.see(question1.title);
  I.see(question1.description);
});

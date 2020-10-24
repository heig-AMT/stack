Feature('answers');

const {
  I,
  registerPage,
  loginPage,
  questionsPage,
  logoutPage,
  answersPage
} = inject();

Scenario('Add an answer', (I, logoutPage, registerPage, questionsPage, answersPage) => {
  registerPage.register();
  const question = questionsPage.addQuestion();
  logoutPage.logout();

  const user2 = registerPage.register();
  I.click("Questions")
  I.click(question.title);

  const answer = answersPage.addAnswer();

  I.see(user2.user);
  I.see(answer);
});

Scenario('Cannot add an answer, when not logged in', (I, logoutPage, registerPage, questionsPage, answersPage) => {
  registerPage.register();
  const question = questionsPage.addQuestion();
  logoutPage.logout();

  I.click("Questions")
  I.click(question.title);

  const answer = answersPage.addAnswer();
  I.dontSee(answer);
  I.waitInUrl('/login', 2);
});

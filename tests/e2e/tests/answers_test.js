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

Scenario('Cannot add an answer, when not logged in', (I, logoutPage, registerPage, questionsPage) => {
  registerPage.register();
  const question = questionsPage.addQuestion();
  logoutPage.logout();

  I.click("Questions")
  I.click(question.title);

  I.click("To answer, please login");

  I.waitInUrl('/login', 2);
});

Scenario('Can delete my answer', (I, registerPage, questionsPage, answersPage) => {
  registerPage.register();
  const question = questionsPage.addQuestion();

  I.click("Questions");
  I.click(question.title);

  const answer = answersPage.addAnswer();
  I.see(answer);

  I.click("Delete");

  I.dontSee(answer);
});

Scenario('Cannot delete someone\'s answer', (I, logoutPage, registerPage, questionsPage, answersPage) => {
  registerPage.register();
  const question = questionsPage.addQuestion();

  I.click("Questions");
  I.click(question.title);

  answersPage.addAnswer();

  logoutPage.logout();

  I.click("Questions");
  I.click(question.title);

  I.dontSee("Delete");
});

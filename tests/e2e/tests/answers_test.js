Feature('answers');

const {
  I,
  registerPage,
  loginPage,
  questionsPage,
  logoutPage,
  answersPage
} = inject();

Scenario('Add an answer', (I, logoutPage, registerPage, questionsPage) => {
  registerPage.register();
  const question = questionsPage.addQuestion();
  logoutPage.logout();

  const user2 = registerPage.register();
  I.click("Questions")
  I.click(question.title);

  const answer = answersPage.addAnswer();

  I.see(user2.user)
  I.see(answer)
});

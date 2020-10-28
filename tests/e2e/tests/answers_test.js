Feature('answers');

const {
  I,
  registerPage,
  profilePage,
  questionsPage,
  answersPage
} = inject();

Scenario('Add an answer', (I, profilePage, registerPage, questionsPage, answersPage) => {
  registerPage.register();
  const question = questionsPage.addQuestion();
  profilePage.logout();

  const user2 = registerPage.register();
  I.click("Questions")
  I.click(question.title);

  const answer = answersPage.addAnswer();

  I.see(user2.user);
  I.see(answer);
});

Scenario('Cannot add an answer, when not logged in', (I, profilePage, registerPage, questionsPage) => {
  registerPage.register();
  const question = questionsPage.addQuestion();
  profilePage.logout();

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

Scenario('Cannot delete someone\'s answer', (I, profilePage, registerPage, questionsPage, answersPage) => {
  registerPage.register();
  const question = questionsPage.addQuestion();

  I.click("Questions");
  I.click(question.title);

  answersPage.addAnswer();

  profilePage.logout();

  I.click("Questions");
  I.click(question.title);

  I.dontSee("Delete");
});

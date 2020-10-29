Feature('answers');

const {
  I,
  profilePage,
  questionsPage,
  answersPage
} = inject();

Scenario('Add an answer', ({ I, profilePage, questionsPage, answersPage }) => {
  profilePage.register();
  const question = questionsPage.addQuestion();
  profilePage.logout();

  const user2 = profilePage.register();
  I.click("Questions")
  I.click(question.title);

  const answer = answersPage.addAnswer();

  I.see(user2.user);
  I.see(answer);
});

Scenario('Cannot add an answer, when not logged in', ({ I, profilePage, questionsPage }) => {
  profilePage.register();
  const question = questionsPage.addQuestion();
  profilePage.logout();

  I.click("Questions")
  I.click(question.title);

  I.click("To answer, please login");

  I.waitInUrl('/login', 2);
});

Scenario('Can delete my answer', ({ I, questionsPage, answersPage }) => {
  profilePage.register();
  const question = questionsPage.addQuestion();

  I.click("Questions");
  I.click(question.title);

  const answer = answersPage.addAnswer();
  I.see(answer);

  answersPage.deleteAnswer();

  I.dontSee(answer);
});

Scenario('Cannot delete someone\'s answer', ({ I, profilePage, questionsPage, answersPage }) => {
  profilePage.register();
  const question = questionsPage.addQuestion();

  I.click("Questions");
  I.click(question.title);

  answersPage.addAnswer();

  profilePage.logout();

  I.click("Questions");
  I.click(question.title);

  I.dontSeeElement({css: 'form[action^=deleteAnswer] input[type=submit]'});
});

Scenario('When a user deletes his profile, his answers are deleted', ({ I, profilePage, questionsPage }) => {
  profilePage.register();
  const question = questionsPage.addQuestion();

  profilePage.logout();
  const register = profilePage.register();

  I.amOnPage('/questions');
  I.click(question.title);

  const answer = answersPage.addAnswer();

  profilePage.deleteAccount(register.password);

  I.amOnPage('/questions');
  I.click(question.title);

  I.dontSee(answer);
});

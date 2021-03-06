Feature('addQuestions');

const {
  I,
  questionsPage,
  profilePage
} = inject();

Scenario('When adding a question, must be redirected to it\'s own page', ({ I, profilePage, questionsPage }) => {
  profilePage.register();

  const question = questionsPage.addQuestion();

  I.waitInUrl("/question?id", 2);
  I.see(question.title);
  I.see(question.description);
});

Scenario('When adding a question, it must appear in question list', ({ I, profilePage, questionsPage }) => {
  profilePage.register();

  const question = questionsPage.addQuestion();

  I.click("Questions")
  I.see(question.title);
  I.see(question.description);
});

Scenario('/ask redirects to /login when logged out', ({ I, profilePage }) => {
  profilePage.register();

  profilePage.logout();

  I.amOnPage('/ask');
  I.waitInUrl('/login', 2);
});

Scenario('Filter questions', ({ I, profilePage, questionsPage }) => {
  const register = profilePage.register();

  profilePage.login(register.user, register.password);

  const question1 = questionsPage.addQuestion();
  const question2 = questionsPage.addQuestion();
  const question3 = questionsPage.addQuestion();
  const question4 = questionsPage.addQuestion();

  // Verify title search
  I.fillField("Search by content in the title or in the description !", question1.title.substring(1,3));
  I.click("input[alt=submit]");
  I.see(question1.title);

  // Verify description search
  I.fillField("Search by content in the title or in the description !", question1.description.substring(1,3));
  I.click("input[alt=submit]");
  I.see(question1.description);

  // Verify that the other tasks are not shown
  I.fillField("Search by content in the title or in the description !", question2.title);
  I.click("input[alt=submit]");
  I.see(question2.title);
  I.dontSee(question1.title);
  I.dontSee(question3.title);
  I.dontSee(question4.title);
  I.dontSee(question1.description);
  I.dontSee(question3.description);
  I.dontSee(question4.description);
});

Scenario('When a user deletes his profile, his questions are deleted', ({ I, profilePage, questionsPage }) => {
  const register = profilePage.register();
  const question = questionsPage.addQuestion();

  I.amOnPage('/questions');
  I.see(question.title);

  profilePage.deleteAccount(register.password);

  I.amOnPage('/questions');
  I.dontSee(question.title);
});

Scenario('A question can be deleted by it\'s creator', ({ I, profilePage, questionsPage }) => {
  profilePage.register();
  const question = questionsPage.addQuestion();

  I.see(question.title);
  I.see(question.description);

  questionsPage.deleteQuestion();

  I.dontSee(question.title);
});

Scenario('A question cannot be deleted by not it\'s creator', ({ I, profilePage, questionsPage }) => {
  profilePage.register();
  const question = questionsPage.addQuestion();

  I.see(question.title);
  I.see(question.description);

  profilePage.logout();
  profilePage.register();

  I.click(question.title);

  I.dontSeeElement({css: 'form[action^=deleteQuestion] input[type=submit]'});
});

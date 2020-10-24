Feature('addQuestions');

const { I, registerPage, loginPage, questionsPage, logoutPage } = inject();

Scenario('When adding a question, must be redirected to it\'s own page', (I, registerPage, questionsPage) => {
  registerPage.register();

  const question = questionsPage.addQuestion();

  I.waitInUrl("/question?id", 2);
  I.see(question.title);
  I.see(question.description);
});

Scenario('When adding a question, it must appear in question list', (I, registerPage, questionsPage) => {
  registerPage.register();

  const question = questionsPage.addQuestion();

  I.click("Questions")
  I.see(question.title);
  I.see(question.description);
});

Scenario('/ask redirects to /login when logged out', (I, registerPage, logoutPage) => {
  registerPage.register();

  logoutPage.logout();

  I.amOnPage('/ask');
  I.waitInUrl('/login', 2);
});

Scenario('Filter questions', (I, registerPage, loginPage, questionsPage) => {
  const register = registerPage.register();

  loginPage.login(register.user, register.password);

  const question1 = questionsPage.addQuestion();
  const question2 = questionsPage.addQuestion();
  const question3 = questionsPage.addQuestion();
  const question4 = questionsPage.addQuestion();

  // Verify title search
  I.fillField("Search by content in the title, description or tags !", question1.title.substring(1,3));
  I.click("input[alt=submit]");
  I.see(question1.title);

  // Verify description search
  I.fillField("Search by content in the title, description or tags !", question1.description.substring(1,3));
  I.click("input[alt=submit]");
  I.see(question1.description);

  // Verify that the other tasks are not shown
  I.fillField("Search by content in the title, description or tags !", question2.title);
  I.click("input[alt=submit]");
  I.see(question2.title);
  I.dontSee(question1.title);
  I.dontSee(question3.title);
  I.dontSee(question4.title);
  I.dontSee(question1.description);
  I.dontSee(question3.description);
  I.dontSee(question4.description);
});

Feature('addQuestions');

const { I, registerPage, loginPage, questionsPage, logoutPage } = inject();

Scenario('Add a question', (I, registerPage, questionsPage) => {
  registerPage.register();

  const question = questionsPage.addQuestion();

  I.see(question.title);
  I.see(question.description);
});

Scenario('Filter questions', (I, registerPage, loginPage, questionsPage) => {
  const register = registerPage.register();

  loginPage.login(register.email, register.password);

  const question1 = questionsPage.addQuestion();
  const question2 = questionsPage.addQuestion();
  const question3 = questionsPage.addQuestion();
  const question4 = questionsPage.addQuestion();

  // Verify title search
  I.fillField("Search", question1.title.substring(1,3));
  I.click("Filter");
  I.see(question1.title);

  // Verify description search
  I.fillField("Search", question1.description.substring(1,3));
  I.click("Filter");
  I.see(question1.description);

  // Verify that the other tasks are not shown
  I.fillField("Search", question2.title);
  I.click("Filter");
  I.see(question2.title);
  I.dontSee(question1.title);
  I.dontSee(question3.title);
  I.dontSee(question4.title);
  I.dontSee(question1.description);
  I.dontSee(question3.description);
  I.dontSee(question4.description);
});

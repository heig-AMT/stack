Feature('questionList');

const {
  I,
  questionsPage,
  profilePage
} = inject();

Scenario('See questions when not logged in', (I, profilePage, questionsPage) => {
  profilePage.register();

  const question1 = questionsPage.addQuestion();
  profilePage.logout();

  I.amOnPage('/questions');
  I.see(question1.title);
  I.see(question1.description);
});

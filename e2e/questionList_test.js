Feature('questionList');

Scenario('Test that the questions are shown', (I) => {
  I.amOnPage("http://localhost:8080/mvc-simple/questions");
  I.see("My first title");
  I.see("Something else");
});

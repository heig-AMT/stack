Feature('questionList');

Scenario('Visiting the question page', (I) => {
  I.amOnPage("http://localhost:8080/mvc-simple/questions");

  // Check that we have table headers
  I.see("Title");
  I.see("Description");
});

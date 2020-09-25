Feature('questionList');

Scenario('Visiting the question page', (I) => {
  I.amOnPage("http://localhost:8080/mvc-simple/questions");

  // Check that we have table headers
  I.see("Title");
  I.see("Description");

  // Check that we have pre-made questions, for now
  I.see("Why doesn't this program run ?");
  I.see("Can we inject an EJB in a JSP ?");
});

Feature('questionList');

Scenario('Visiting the question page', (I) => {
  I.amOnPage("/questions");

  // TODO : Figure out what's actually displayed on an empty questions page.
  // I.see("Title");
  // I.see("Description");
});

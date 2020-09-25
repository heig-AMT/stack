Feature('homepage');

Scenario('Test that the homepage displays correctly', (I) => {
  I.amOnPage("http://localhost:8080/mvc-simple/");

  I.see("stackunderflow");
  I.see("Ask anything code related, and get insulted by your peers!");
});

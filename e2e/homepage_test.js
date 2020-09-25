Feature('homepage');

Scenario('Visiting the homepage', (I) => {
  I.amOnPage("http://localhost:8080/mvc-simple/");

  I.see("stackunderflow");
  I.see("Ask anything code related, and get insulted by your peers!");
});

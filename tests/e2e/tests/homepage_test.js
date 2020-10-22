Feature('homepage');

Scenario('Visiting the homepage', (I) => {
  I.amOnPage("/");

  I.see("stackunderflow");
  I.see("Ask anything code related, and get insulted by your peers!");
});

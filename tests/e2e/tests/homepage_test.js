Feature('homepage');

Scenario('Visiting the homepage', (I) => {
  I.amOnPage("/");

  I.see("stackunderflow");
  I.see("Ask anything like our");
  I.see("code related questions, answer them like");
  I.see("others did, and get insulted by");
  I.see("of your peers!");
});

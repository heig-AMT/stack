Feature('register');

Scenario('user wants to register', (I) => {
  I.amOnPage("http://localhost:8080/mvc-simple/register");

  I.see("Register an account");

  I.fillField("username", "mario2@gmail.com");
  I.fillField("Password", "1234");
  I.click("input[type=submit]");

  I.see("Title");
  I.see("Description");
});

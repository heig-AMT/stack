Feature('register');

const { I, registerPage } = inject();

Scenario('user wants to register', (I, registerPage) => {
  registerPage.register();

  I.amOnPage("http://localhost:8080/questions");

  I.see("Title");
  I.see("Description");
});

Feature('register');

const { I, registerPage } = inject();

Scenario('user wants to register', (I, registerPage) => {
  registerPage.register();

  I.amOnPage("/questions");

  // TODO : Figure out what's actually displayed on an empty questions page.
  // I.see("Title");
  // I.see("Description");
});

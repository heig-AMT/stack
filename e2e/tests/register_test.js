Feature('register');

const { I, registerPage } = inject();

Scenario('User is redirect to /questions after registering', (I, registerPage) => {
  registerPage.register();

  I.seeInCurrentUrl('/questions');
});

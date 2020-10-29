Feature('register');

const {
  I,
  profilePage
} = inject();

Scenario('User is redirect to /questions after registering', (I, profilePage) => {
  profilePage.register();

  I.seeInCurrentUrl('/questions');
});

Feature('profile');

const {
  I,
  profilePage
} = inject();

Scenario('User is redirect to /questions after registering', ({ I, profilePage }) => {
  profilePage.register();

  I.seeInCurrentUrl('/questions');
});

Scenario('User can change his password', ({ I, profilePage }) => {
  const register = profilePage.register();
  profilePage.logout();

  profilePage.login(register.user, register.password);
  const newPassword = profilePage.changePassword(register.password);

  profilePage.logout();
  profilePage.login(register.user, newPassword);

  I.dontSee("form input[value=Login]");
});

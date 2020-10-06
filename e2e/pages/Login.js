const { I } = inject();

module.exports = {
  login(username, password) {
    I.amOnPage("/login");
    I.fillField("Username", username);
    I.fillField("Password", password);
  }
}

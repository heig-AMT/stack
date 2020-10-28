const { I } = inject();

module.exports = {
  login(username, password) {
    I.amOnPage("/login");

    I.fillField("Username", username);
    I.fillField("Password", password);

    I.click("form input[value=Login]");
  },

  logout() {
    I.click("form input[value='Log out']");
  }
}

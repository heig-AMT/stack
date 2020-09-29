const { I } = inject();

module.exports = {
  login(username, password) {
    I.amOnPage("http://localhost:8080/login");
    I.fillField("Username", username);
    I.fillField("Password", password);
  }
}

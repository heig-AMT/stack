const { I } = inject();

module.exports = {
  register() {
    const user = Math.random().toString(36).substring(7);
    const email = user + "@example.org";
    const password = Math.random().toString(36).substring(7);

    I.amOnPage("http://localhost:8080/register");

    I.see("Register an account");

    I.fillField("username", email);
    I.fillField("Password", password);
    I.click("input[type=submit]");

    return { email, password }
  }
}

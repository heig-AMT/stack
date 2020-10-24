const { I } = inject();

module.exports = {
  register() {
    const user = Math.random().toString(36).substring(7);
    const password = Math.random().toString(36).substring(7);

    I.amOnPage("/register");

    I.see("Register an account");

    I.fillField("username", user);
    I.fillField("Password", password);
    I.click("form input[value=Register]");

    return { user, password }
  }
}

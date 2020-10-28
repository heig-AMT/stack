const { I } = inject();

function randomString(length) {
   var string     = '';
   var characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
   for ( var i = 0; i < length; i++ ) {
      string += characters.charAt(Math.floor(Math.random() * characters.length));
   }
   return string;
}

module.exports = {
  register() {
    const user = randomString(10);
    const password = randomString(10);

    I.amOnPage("/register");

    I.see("Register an account");

    I.fillField("username", user);
    I.fillField("Password", password);
    I.click("form input[value=Register]");

    return { user, password }
  }
}

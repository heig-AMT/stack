const { I } = inject();

module.exports = {
  logout() {
    I.click("form input[value='Log out']");
  }
}

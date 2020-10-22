const uid = require("uuid");

Feature('error 404 page');

const { I, registerPage } = inject();

Scenario('user accesses non existing page', (I, registerPage) => {
  registerPage.register();

  I.amOnPage("/" + uid.v4());
  I.see("This page was taken by 404 aliens");
});

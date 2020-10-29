const uid = require("uuid");

Feature('error 404 page');

const {
  I,
  profilePage
} = inject();

Scenario('user accesses non existing page', ({ I, profilePage }) => {
  profilePage.register();

  I.amOnPage("/" + uid.v4());
  I.see("This page was taken by 404 aliens");
});

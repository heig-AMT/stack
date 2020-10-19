const uid = require("uuid");

const { I } = inject();

module.exports = {
  addQuestion() {
    const randomTitle = uid.v4();
    const randomDesc = uid.v4();

    I.wait(0.5)
    I.amOnPage("/ask");
    I.seeInCurrentUrl('/ask')

    I.see("Ask a question");

    I.fillField("#title", randomTitle);
    I.fillField("#description", randomDesc);

    I.click("Publish question");

    return { title: randomTitle, description: randomDesc }
  }
}
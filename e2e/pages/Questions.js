const uid = require("uuid");

const { I } = inject();

module.exports = {
  addQuestion() {
    const randomTitle = uid.v4();
    const randomDesc = uid.v4();

    I.amOnPage("/ask");

    I.see("Ask a question");

    I.fillField("Title", randomTitle);
    I.fillField("Description", randomDesc);

    I.click("Publish");

    return { title: randomTitle, description: randomDesc }
  }
}

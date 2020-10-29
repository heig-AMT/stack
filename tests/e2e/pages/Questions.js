const uid = require("uuid");

const { I } = inject();

module.exports = {
  addQuestion() {
    const randomTitle = uid.v4();
    const randomDesc = uid.v4();

    I.amOnPage("/ask");

    I.see("Ask a question");

    I.fillField("#title", randomTitle);
    I.fillField("#description", randomDesc);

    I.click("Publish question");

    return { title: randomTitle, description: randomDesc }
  },

  deleteQuestion() {
    // Have to be on the question's view, not in the list of questions
    I.seeElement({css: 'form[action^=deleteQuestion] input[type=submit]'});
    I.click({css: 'form[action^=deleteQuestion] input[type=submit]'});
  }
}

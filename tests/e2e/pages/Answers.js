const uid = require("uuid");

const { I } = inject();

module.exports = {
  // Must already be on the correct page
  addAnswer() {
    const randomAnswer = uid.v4();

    I.fillField("#body", randomAnswer);

    I.click("Answer question");

    return randomAnswer
  }
}

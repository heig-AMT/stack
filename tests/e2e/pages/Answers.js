const uid = require("uuid");

const { I } = inject();

module.exports = {
  // Must already be on the correct page
  addAnswer() {
    const randomAnswer = uid.v4();

    I.fillField("#body", randomAnswer);

    I.click("Answer question");

    return randomAnswer
  },

  upvoteForAns(n) {
    I.click({css: ".voteBox:nth-of-type(" + n + ") form:nth-of-type(1) input[type=\"image\"]"});
  },

  downvoteForAns(n) {
    I.click({css: ".voteBox:nth-of-type(" + n + ") form:nth-of-type(2) input[type=\"image\"]"});
  },

  async getVotesForAns(n) {
    votes = { up: 0, down: 0 }

    votes.up = await I.grabTextFrom(".voteBox:nth-of-type(" + n + ") span:nth-of-type(1)")
    votes.down = await I.grabTextFrom(".voteBox:nth-of-type(" + n + ") span:nth-of-type(2)")

    return votes;
  }
}

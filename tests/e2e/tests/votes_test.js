Feature('votes');

const assert = require('assert');
const {
  I,
  registerPage,
  loginPage,
  questionsPage,
  logoutPage,
  answersPage
} = inject();


Scenario('Votes are updated', async (I, registerPage, questionsPage, answersPage) => {
  let numUpvotes = 0;
  let numDownvotes = 0;

  registerPage.register();
  const question = questionsPage.addQuestion();
  I.click("Questions")
  I.click(question.title);

  answersPage.addAnswer();

  numUpvotes = await I.grabTextFrom('.voteBox:nth-of-type(1) span:nth-of-type(1)')
  numDownvotes = await I.grabTextFrom('.voteBox:nth-of-type(1) span:nth-of-type(2)')

  assert.equal(0, numUpvotes);
  assert.equal(0, numDownvotes);

  I.click({css: '.voteBox:nth-of-type(1) form:nth-of-type(1) input[type="image"]'});

  numUpvotes = await I.grabTextFrom('.voteBox:nth-of-type(1) span:nth-of-type(1)')
  numDownvotes = await I.grabTextFrom('.voteBox:nth-of-type(1) span:nth-of-type(2)')

  assert.equal(1, numUpvotes);
  assert.equal(0, numDownvotes);

  I.click({css: '.voteBox:nth-of-type(1) form:nth-of-type(2) input[type="image"]'});

  numUpvotes = await I.grabTextFrom('.voteBox:nth-of-type(1) span:nth-of-type(1)')
  numDownvotes = await I.grabTextFrom('.voteBox:nth-of-type(1) span:nth-of-type(2)')

  assert.equal(0, numUpvotes);
  assert.equal(1, numDownvotes);
});

Scenario('Cannot vote twice', async (I, registerPage, questionsPage, answersPage) => {
  let numUpvotes = 0;
  let numDownvotes = 0;

  registerPage.register();
  const question = questionsPage.addQuestion();
  I.click("Questions")
  I.click(question.title);

  answersPage.addAnswer();

  numUpvotes = await I.grabTextFrom('.voteBox:nth-of-type(1) span:nth-of-type(1)')
  numDownvotes = await I.grabTextFrom('.voteBox:nth-of-type(1) span:nth-of-type(2)')

  assert.equal(0, numUpvotes);
  assert.equal(0, numDownvotes);

  I.click({css: '.voteBox:nth-of-type(1) form:nth-of-type(1) input[type="image"]'});
  I.dontSeeElement({css: '.voteBox:nth-of-type(1) form:nth-of-type(1) input[type="image"]'});

  numUpvotes = await I.grabTextFrom('.voteBox:nth-of-type(1) span:nth-of-type(1)')
  numDownvotes = await I.grabTextFrom('.voteBox:nth-of-type(1) span:nth-of-type(2)')

  assert.equal(1, numUpvotes);
  assert.equal(0, numDownvotes);

  I.click({css: '.voteBox:nth-of-type(1) form:nth-of-type(2) input[type="image"]'});
  I.dontSeeElement({css: '.voteBox:nth-of-type(1) form:nth-of-type(2) input[type="image"]'});

  numUpvotes = await I.grabTextFrom('.voteBox:nth-of-type(1) span:nth-of-type(1)')
  numDownvotes = await I.grabTextFrom('.voteBox:nth-of-type(1) span:nth-of-type(2)')

  assert.equal(0, numUpvotes);
  assert.equal(1, numDownvotes);
});

Scenario('Cannot vote when logged out', async (I, registerPage, logoutPage, questionsPage, answersPage) => {
  registerPage.register();
  const question = questionsPage.addQuestion();
  I.click("Questions")
  I.click(question.title);

  answersPage.addAnswer();
  logoutPage.logout();

  I.dontSeeElement({css: '.voteBox:nth-of-type(1) form:nth-of-type(1) input[type="image"]'});
  I.dontSeeElement({css: '.voteBox:nth-of-type(1) form:nth-of-type(2) input[type="image"]'});
});

Scenario('Multiple people can vote', async (I, logoutPage, registerPage, questionsPage, answersPage) => {
  let numUpvotes = 0;
  let numDownvotes = 0;

  registerPage.register();
  const question = questionsPage.addQuestion();
  I.click("Questions")
  I.click(question.title);

  answersPage.addAnswer();

  numUpvotes = await I.grabTextFrom('.voteBox:nth-of-type(1) span:nth-of-type(1)')
  numDownvotes = await I.grabTextFrom('.voteBox:nth-of-type(1) span:nth-of-type(2)')

  assert.equal(0, numUpvotes);
  assert.equal(0, numDownvotes);

  I.click({css: '.voteBox:nth-of-type(1) form:nth-of-type(1) input[type="image"]'});

  numUpvotes = await I.grabTextFrom('.voteBox:nth-of-type(1) span:nth-of-type(1)')
  numDownvotes = await I.grabTextFrom('.voteBox:nth-of-type(1) span:nth-of-type(2)')

  assert.equal(1, numUpvotes);
  assert.equal(0, numDownvotes);

  logoutPage.logout();

  // Second user
  registerPage.register();

  I.click("Questions")
  I.click(question.title);
  I.click({css: '.voteBox:nth-of-type(1) form:nth-of-type(1) input[type="image"]'});

  numUpvotes = await I.grabTextFrom('.voteBox:nth-of-type(1) span:nth-of-type(1)')
  numDownvotes = await I.grabTextFrom('.voteBox:nth-of-type(1) span:nth-of-type(2)')

  assert.equal(2, numUpvotes);
  assert.equal(0, numDownvotes);

  logoutPage.logout();

  // Third user
  registerPage.register();

  I.click("Questions")
  I.click(question.title);
  I.click({css: '.voteBox:nth-of-type(1) form:nth-of-type(1) input[type="image"]'});

  numUpvotes = await I.grabTextFrom('.voteBox:nth-of-type(1) span:nth-of-type(1)')
  numDownvotes = await I.grabTextFrom('.voteBox:nth-of-type(1) span:nth-of-type(2)')

  assert.equal(3, numUpvotes);
  assert.equal(0, numDownvotes);
});

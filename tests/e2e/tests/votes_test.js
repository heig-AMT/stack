Feature('votes');

const assert = require('assert');
const {
  I,
  questionsPage,
  profilePage,
  answersPage
} = inject();


Scenario('Votes are updated', async ({ I, profilePage, questionsPage, answersPage }) => {
  let votes = null;

  profilePage.register();
  const question = questionsPage.addQuestion();
  I.click("Questions")
  I.click(question.title);

  answersPage.addAnswer();

  // initial value
  votes = await answersPage.getVotesForAns(1);
  assert.equal(0, votes.up);
  assert.equal(0, votes.down);

  // upvote
  answersPage.upvoteForAns(1);

  votes = await answersPage.getVotesForAns(1);
  assert.equal(1, votes.up);
  assert.equal(0, votes.down);

  // downvote
  answersPage.downvoteForAns(1);

  votes = await answersPage.getVotesForAns(1);
  assert.equal(0, votes.up);
  assert.equal(1, votes.down);
});

Scenario('Cannot vote twice', async ({ I, profilePage, questionsPage, answersPage }) => {
  let votes = null;

  profilePage.register();
  const question = questionsPage.addQuestion();
  I.click("Questions")
  I.click(question.title);

  answersPage.addAnswer();

  // initial votes value
  votes = await answersPage.getVotesForAns(1);
  assert.equal(0, votes.up);
  assert.equal(0, votes.down);

  // upvote
  answersPage.upvoteForAns(1);
  I.dontSeeElement({css: '.voteBox:nth-of-type(1) form:nth-of-type(1) input[type="image"]'});

  votes = await answersPage.getVotesForAns(1);
  assert.equal(1, votes.up);
  assert.equal(0, votes.down);

  // downvote
  answersPage.downvoteForAns(1);
  I.dontSeeElement({css: '.voteBox:nth-of-type(1) form:nth-of-type(2) input[type="image"]'});

  votes = await answersPage.getVotesForAns(1);
  assert.equal(0, votes.up);
  assert.equal(1, votes.down);
});

Scenario('Cannot vote when logged out', async ({ I, profilePage, questionsPage, answersPage }) => {
  profilePage.register();
  const question = questionsPage.addQuestion();
  I.click("Questions")
  I.click(question.title);

  answersPage.addAnswer();
  profilePage.logout();

  I.dontSeeElement({css: '.voteBox:nth-of-type(1) form:nth-of-type(1) input[type="image"]'});
  I.dontSeeElement({css: '.voteBox:nth-of-type(1) form:nth-of-type(2) input[type="image"]'});
});

Scenario('Multiple people can vote', async ({ I, profilePage, questionsPage, answersPage }) => {
  let votes = null;

  profilePage.register();
  const question = questionsPage.addQuestion();
  I.click("Questions")
  I.click(question.title);

  answersPage.addAnswer();

  // initial votes value
  votes = await answersPage.getVotesForAns(1);
  assert.equal(0, votes.up);
  assert.equal(0, votes.down);

  // First user's upvote
  answersPage.upvoteForAns(1);
  votes = await answersPage.getVotesForAns(1);
  assert.equal(1, votes.up);
  assert.equal(0, votes.down);

  profilePage.logout();

  // Second user
  profilePage.register();

  I.click("Questions")
  I.click(question.title);
  answersPage.upvoteForAns(1);

  votes = await answersPage.getVotesForAns(1);
  assert.equal(2, votes.up);
  assert.equal(0, votes.down);

  profilePage.logout();

  // Third user
  profilePage.register();

  I.click("Questions")
  I.click(question.title);
  answersPage.upvoteForAns(1);

  votes = await answersPage.getVotesForAns(1);
  assert.equal(3, votes.up);
  assert.equal(0, votes.down);
});

Scenario('When a user deletes his profile, his votes are deleted', async ({ I, profilePage, questionsPage }) => {
  profilePage.register();
  const question = questionsPage.addQuestion();
  answersPage.addAnswer();

  profilePage.logout();
  const register = profilePage.register();

  I.amOnPage('/questions');
  I.click(question.title);

  answersPage.upvoteForAns(1);
  votes = await answersPage.getVotesForAns(1);
  assert.equal(1, votes.up);
  assert.equal(0, votes.down);

  profilePage.deleteAccount(register.password);

  I.amOnPage('/questions');
  I.click(question.title);

  votes = await answersPage.getVotesForAns(1);
  assert.equal(0, votes.up);
  assert.equal(0, votes.down);
});

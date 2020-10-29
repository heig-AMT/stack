const { setHeadlessWhen } = require('@codeceptjs/configure');

setHeadlessWhen(process.env.HEADLESS);

exports.config = {
  tests: './tests/*_test.js',
  output: './output',
  helpers: {
    Puppeteer: {
      url: 'http://localhost:8080',
      show: false,
      windowSize: '1600x900',
      waitForAction: 300,
    }
  },
  include: {
    I: './steps_file.js',
    questionsPage: './pages/Questions.js',
    answersPage: './pages/Answers.js',
    profilePage: './pages/Profile.js',
  },
  bootstrap: null,
  mocha: {},
  name: 'e2e',
  plugins: {
    retryFailedStep: {
      enabled: true,
      retries: 10,
    },
    screenshotOnFail: {
      enabled: true
    }
  }
}

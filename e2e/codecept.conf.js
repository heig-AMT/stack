const { setHeadlessWhen } = require('@codeceptjs/configure');

setHeadlessWhen(process.env.HEADLESS);

exports.config = {
  tests: './tests/*_test.js',
  output: './output',
  helpers: {
    Puppeteer: {
      url: 'http://localhost',
      show: false,
      windowSize: '1600x900'
    }
  },
  include: {
    I: './steps_file.js',
    loginPage: './pages/Login.js',
    registerPage: './pages/Register.js',
    questionsPage: './pages/Questions.js',
  },
  bootstrap: null,
  mocha: {},
  name: 'e2e',
  plugins: {
    retryFailedStep: {
      enabled: true
    },
    screenshotOnFail: {
      enabled: true
    }
  }
}

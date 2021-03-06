const { setHeadlessWhen } = require('@codeceptjs/configure');

setHeadlessWhen(process.env.HEADLESS);

exports.config = {
  tests: './tests/*_test.js',
  output: './output',
  helpers: {
    WebDriver: {
      url: 'http://localhost:8080',
      browser: 'chrome',
      host: '127.0.0.1',
      port: 4444,
      path: '/wd/hub',
      restart: false,
      windowSize: '1920x1680',
      manualStart: false,
      desiredCapabilities: {
        chromeOptions: {
          args: ["--headless", "--disable-gpu", "--no-sandbox", "--disable-dev-shm-usage"]
        }
      }
    },
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
      enabled: false,
      retries: 0,
    },
    screenshotOnFail: {
      enabled: true
    }
  }
}

import { v4 as uuidv4 } from 'uuid';

const { I } = inject();

module.exports = {
  addQuestion() {
    const randomTitle = uuidv4();
    const randomDesc = uuidv4();

    I.amOnPage("http://localhost:8080/ask");

    I.see("Ask a question");

    I.fillField("Title", randomTitle);
    I.fillField("Description", randomDesc);

    I.click("Publish");

    return { title: randomTitle, description: randomDesc }
  }
}

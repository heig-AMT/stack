const { I } = inject();

module.exports = {
  addQuestion() {
    const randomTitle = Math.random().toString(36).substring(7);
    const randomDesc = Math.random().toString(36).substring(7);

    I.amOnPage("http://localhost:8080/ask");

    I.see("Ask a question");

    I.fillField("Title", randomTitle);
    I.fillField("Description", randomDesc);

    I.click("Publish");

    return { title: randomTitle, description: randomDesc }
  }
}

const { I } = inject();

module.exports = {
  addQuestion() {
    const randomTitle = Math.random().toString(36).substring(7) + " whadda ya think?";
    const description = "This is my super question!";

    I.amOnPage("http://localhost:8080/ask");

    I.see("Ask a question");

    I.fillField("Title", randomTitle);
    I.fillField("Description", description);

    I.click("Publish");

    return { title: randomTitle, description: description }
  }
}

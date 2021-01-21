package ch.heigvd.amt.stack.domain.gamification;

public enum GamificationCategory {
  Questions(
      "questions",
      "Questions",
      "Addition of new questions"
  ),
  Answers(
      "answers",
      "Answers",
      "Addition of new answers"
  ),
  Comments(
      "comments",
      "Comments",
      "Addition of new comments and votes for them"
  ),
  ;

  private final String name;
  private final String description;
  private final String title;

  /* private */ GamificationCategory(
      String name,
      String title,
      String description
  ) {
    this.name = name;
    this.title = title;
    this.description = description;
  }

  public String getName() {
    return name;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }
}

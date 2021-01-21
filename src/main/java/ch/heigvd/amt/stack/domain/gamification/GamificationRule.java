package ch.heigvd.amt.stack.domain.gamification;

public enum GamificationRule {

  NewQuestionGrantsQuestionPoints(
      "newQuestionGrantsQuestionPoints",
      GamificationCategory.Questions,
      GamificationEvent.NEW_QUESTION,
      10
  ),
  NewAnswersGrantsAnswerPoints(
      "newAnswerGrantsAnswerPoints",
      GamificationCategory.Answers,
      GamificationEvent.NEW_ANSWER,
      20
  ),
  NewCommentGrantsCommentPoints(
      "newCommentGrantsCommentPoints",
      GamificationCategory.Comments,
      GamificationEvent.NEW_COMMENT,
      25
  ),
  UpvoteGrantsUpvotePoints(
      "upvoteGrantsUpvotePoints",
      GamificationCategory.Comments,
      GamificationEvent.UPVOTE,
      10
  ),
  DownvoteGrantsDownvotePoints(
      "downvoteGrantsDownvotePoints",
      GamificationCategory.Comments,
      GamificationEvent.DOWNVOTE,
      0
  ),
  AcceptAnswerGrantsSelectionPoints(
      "acceptAnswerGrantsSelectionPoints",
      GamificationCategory.Questions,
      GamificationEvent.SELECTION,
      5
  ),
  ;

  private final String name;
  private final GamificationCategory category;
  private final GamificationEvent event;
  private final int points;

  /* private */ GamificationRule(
      String name,
      GamificationCategory category,
      GamificationEvent event,
      int points
  ) {
    this.name = name;
    this.category = category;
    this.event = event;
    this.points = points;
  }

  public String getName() {
    return name;
  }

  public GamificationCategory getCategory() {
    return category;
  }

  public GamificationEvent getEvent() {
    return event;
  }

  public int getPoints() {
    return points;
  }
}

package ch.heigvd.amt.stack.domain.gamification;

import java.util.Arrays;
import java.util.Optional;

/**
 * An enumeration representing the business model for the badges we have at our disposal.
 */
public enum GamificationBadge {

  // Question badges.
  QuestionBadge1(
      "QuestionBadge1",
      GamificationCategory.Questions,
      "Apprentice of questions",
      "First questions badge",
      "https://external-preview.redd.it/Qn69GDdv7jSIicPYwmHF0FWqDNvFBkpJ9_yW_dw_bkk.jpg?auto=webp&s=a80ec851a760e2980fc87eec529dfd452fcf55f6",
      0,
      30
  ),
  QuestionBadge2(
      "QuestionBadge2",
      GamificationCategory.Questions,
      "Mage of questions",
      "Second questions badge",
      "https://preview.redd.it/3lakntnj85f41.jpg?width=497&format=pjpg&auto=webp&s=b6fe3116d52198c1b3a35f073f568e8b5d1f202a",
      29,
      100
  ),
  QuestionBadge3(
      "QuestionBadge3",
      GamificationCategory.Questions,
      "Grand mage of questions",
      "Third questions badge",
      "https://preview.redd.it/oojwlz0zb2w31.png?width=298&format=png&auto=webp&s=4efe79f4e9395b9e2e41a2a7e4dda25ef792515b",
      99,
      1000
  ),
  QuestionBadge4(
      "QuestionBadge4",
      GamificationCategory.Questions,
      "Very grand mage of questions",
      "Fourth questions badge",
      "https://preview.redd.it/eh6fafytbuy01.gif?format=png8&s=3554b6470c9b9192406e275a19e50dda102ac2dd",
      999,
      null
  ),
  //Answer badges
  AnswersBadge1(
      "AnswerBadge1",
      GamificationCategory.Answers,
      "Apprentice of answers",
      "First answers badge",
      "https://i.pinimg.com/originals/9a/29/86/9a29868ac1db20a3656a5412a61abc86.jpg",
      0,
      30
  ),
  AnswersBadge2(
      "AnswerBadge2",
      GamificationCategory.Answers,
      "Maester of answers",
      "Second answers badge",
      "https://preview.redd.it/v9vnqdh5a0h31.png?width=298&format=png&auto=webp&s=a70f957661055a5be0b91652f86aef2e19e0c7c2",
      29,
      100
  ),
  AnswersBadge3(
      "AnswerBadge3",
      GamificationCategory.Answers,
      "Archimaester of answers",
      "Third answers badge",
      "https://i.pinimg.com/originals/46/1d/79/461d79668f54a02da43f99dfcfc7e3be.jpg",
      99,
      1000
  ),
  AnswersBadge4(
      "AnswerBadge4",
      GamificationCategory.Answers,
      "Grand maester of answers",
      "Fourth answers badge",
      "https://i.pinimg.com/originals/dc/4c/0b/dc4c0b475f0167b4e1a08e75a56ba1fc.jpg",
      999,
      null
  ),
  //Comment badges
  CommentBadge1(
      "CommentBadge1",
      GamificationCategory.Comments,
      "Beast of comments",
      "First comments badge",
      "https://cdnb.artstation.com/p/assets/images/images/017/356/521/large/ala-kapustka-menagerie-keeper-2.jpg?1555622659",
      0,
      30
  ),
  CommentBadge2(
      "CommentBadge2",
      GamificationCategory.Comments,
      "Monster of comments",
      "Second comments badge",
      "https://preview.redd.it/43f7hqcqqby41.jpg?width=497&format=pjpg&auto=webp&s=260d11526e15da89a1dbef13bf84334a0b77c484",
      29,
      100
  ),
  CommentBadge3(
      "CommentBadge3",
      GamificationCategory.Comments,
      "Big monster of comments",
      "Third comments badge",
      "https://i.pinimg.com/originals/bd/ac/44/bdac4462d60e295afe0545617c64767c.jpg",
      99,
      1000
  ),
  CommentBadge4(
      "CommentBadge4",
      GamificationCategory.Comments,
      "Terrifying monster of comments",
      "Fourth comments badge",
      "https://i.pinimg.com/originals/80/b1/1c/80b11c6c8caf7ba2221cbdd6fe7eb2fb.jpg",
      999,
      null
  ),
  ;

  /**
   * Returns a {@link GamificationBadge} with the provided name.
   *
   * @param name the name of the badge to retrieve
   * @return the retrieved badge, if any
   */
  public static Optional<GamificationBadge> forName(String name) {
    return Arrays.stream(values())
        .filter(badge -> badge.getName().equals(name))
        .findFirst();
  }

  private final String name;
  private final GamificationCategory category;
  private final String title;
  private final String description;
  private final String imageUrl;
  private final Integer minPoints;
  private final Integer maxPoints;

  /**
   * Creates a new {@link GamificationBadge} that will be attributed to end users.
   *
   * @param name        the name of the badge.
   * @param category    the category of the badge.
   * @param title       the title of the badge.
   * @param description the description of the badge.
   * @param url         the image URl for this badge.
   * @param minPoints   the minimum points of the badge. Null if not provided.
   * @param maxPoints   the maximum points of the badge. Null if not provided.
   */
  /* private */ GamificationBadge(
      String name,
      GamificationCategory category,
      String title,
      String description,
      String url,
      Integer minPoints,
      Integer maxPoints
  ) {
    this.name = name;
    this.category = category;
    this.title = title;
    this.description = description;
    this.imageUrl = url;
    this.minPoints = minPoints;
    this.maxPoints = maxPoints;
  }

  public String getName() {
    return name;
  }

  public GamificationCategory getCategory() {
    return category;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public Optional<Integer> getMinPoints() {
    return Optional.ofNullable(minPoints);
  }

  public Optional<Integer> getMaxPoints() {
    return Optional.ofNullable(maxPoints);
  }
}

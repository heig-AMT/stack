package ch.heigvd.amt.stack.domain.gamification;

import java.util.Arrays;
import java.util.Optional;

/**
 * An enumeration representing the business model for the badges we have at our disposal.
 */
public enum GamificationBadge {

  // Question badges.
  QuestionsApprentice(
      "QBadge1",
      GamificationCategory.Questions,
      "Apprentice of questions",
      "First questions badge",
      "https://external-preview.redd.it/Qn69GDdv7jSIicPYwmHF0FWqDNvFBkpJ9_yW_dw_bkk.jpg?auto=webp&s=a80ec851a760e2980fc87eec529dfd452fcf55f6",
      0,
      30
  );

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

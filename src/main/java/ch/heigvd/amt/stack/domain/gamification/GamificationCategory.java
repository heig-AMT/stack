package ch.heigvd.amt.stack.domain.gamification;

import java.util.Arrays;
import java.util.Optional;

public enum GamificationCategory {
  Questions(
      "questions",
      "Questions",
      "Addition of new questions"
  ),
  ;

  private final String name;
  private final String description;
  private final String title;

  public static Optional<GamificationCategory> forName(String name) {
    return Arrays.stream(GamificationCategory.values()).filter(it -> it.name.equals(name))
        .findFirst();
  }

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

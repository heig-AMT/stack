package ch.heigvd.amt.stack.application;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;

public class IT {

  /**
   * Returns the {@link WebArchive} that should be used for integration tests. It tells Weld which
   * packages to look into.
   */
  public static WebArchive dependencies() {
    return ShrinkWrap.create(WebArchive.class, "arquillian-managed.war")
        // Stack.
        .addPackages(true, "ch.heigvd.amt")
        // BCrypt.
        .addPackages(true, "org.mindrot.jbcrypt")
        // Gamify API.
        .addPackages(true, "ch.heigvd.gamify")
        .addPackages(true, "com.google.gson", "io.gsonfire")
        .addPackages(true, "okhttp3", "okio");
  }
}

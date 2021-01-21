package ch.heigvd.amt.stack.application;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;

public class IT {

  public static WebArchive dependencies() {
    return ShrinkWrap.create(WebArchive.class, "arquillian-managed.war")
        // Stack.
        .addPackages(true, "ch.heigvd.amt")
        // BCrypt.
        .addPackages(true, "org.mindrot.jbcrypt")
        // Gamify API.
        .addPackages(true, "ch.heigvd.gamify")
        .addPackages(true, "okhttp3", "okio");
  }
}

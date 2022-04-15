package nh.publy.backend.graphql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * Contains some flags to modify runtime behaviour for various demonstration purposes
 * <p>
 * Note: you would never use such a thing in real production apps.
 */
public class DemoConfig {

  private static final Logger log = LoggerFactory.getLogger(DemoConfig.class);

  private static final Random random = new Random();

  private final static boolean enableSlowDownReactionSummary = false;

  public static void slowDownReactionSummary() {
    slowDown(enableSlowDownReactionSummary);
  }

  private static void slowDown(boolean enableSlowdown) {
    if (enableSlowdown) {
      try {
        int x = random.nextInt(1300) + 700;
        log.trace("Slowdown for {}ms", x);
        Thread.sleep(x);
      } catch (InterruptedException e) {
        log.info("Interrupted");
      }
    }
  }
}

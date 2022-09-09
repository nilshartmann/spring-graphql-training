package nh.publy.backend.util;

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

  public static void slowDown(String msg) {
    final int x = random.nextInt(200) + 500;
    try {
      log.info("{} - Slowdown for {}ms", msg, x);
      Thread.sleep(x);
    } catch (InterruptedException e) {
      log.info("Interrupted");
    }
  }
}

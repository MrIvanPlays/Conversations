package com.mrivanplays.conversations.base.timeout;

import java.util.concurrent.TimeUnit;

/**
 * Represents a timeout task scheduler.
 *
 * @author MrIvanPlays
 */
public interface TimeoutScheduler {

  /**
   * Schedules the specified {@code task} to run after the specified {@code time} in the specified
   * {@link TimeUnit} {@code timeUnit}
   *
   * @param task task to run
   * @param time time to run after
   * @param timeUnit time unit of the time
   * @return timeout task
   * @see TimeoutTask
   */
  TimeoutTask schedule(Runnable task, long time, TimeUnit timeUnit);

  /**
   * (Optional to implement) Shuts down any executor if needed. Shall be called whenever the program
   * you're using the API into exists.
   */
  default void shutdown() {}
}

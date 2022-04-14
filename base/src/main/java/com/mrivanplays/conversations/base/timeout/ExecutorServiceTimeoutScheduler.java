package com.mrivanplays.conversations.base.timeout;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Represents a {@link TimeoutScheduler} implementation using java's {@link
 * ScheduledExecutorService}
 *
 * @author MrIvanPlays
 */
public class ExecutorServiceTimeoutScheduler implements TimeoutScheduler {

  private final ScheduledExecutorService service;

  public ExecutorServiceTimeoutScheduler() {
    service = Executors.newScheduledThreadPool(3);
  }

  public ExecutorServiceTimeoutScheduler(ScheduledExecutorService service) {
    this.service = service;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TimeoutTask schedule(Runnable task, long time, TimeUnit timeUnit) {
    return new ExecutorServiceTimeoutTask(service.schedule(task, time, timeUnit));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void shutdown() {
    service.shutdown();
  }

  private static class ExecutorServiceTimeoutTask implements TimeoutTask {

    private final ScheduledFuture<?> parent;

    ExecutorServiceTimeoutTask(ScheduledFuture<?> parent) {
      this.parent = parent;
    }

    @Override
    public boolean hasCalled() {
      return parent.isDone();
    }

    @Override
    public void cancel() {
      parent.cancel(false);
    }
  }
}

package com.mrivanplays.conversations.bungee;

import com.mrivanplays.conversations.base.timeout.TimeoutScheduler;
import com.mrivanplays.conversations.base.timeout.TimeoutTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import net.md_5.bungee.api.plugin.Plugin;

class BungeeTimeoutScheduler implements TimeoutScheduler {

  private final ExecutorService service;

  BungeeTimeoutScheduler(Plugin plugin) {
    this.service = plugin.getProxy().getScheduler().unsafe().getExecutorService(plugin);
  }

  @Override
  public TimeoutTask schedule(Runnable task, long time, TimeUnit timeUnit) {
    return new BungeeTimeoutTask(
        service.submit(
            () -> {
              try {
                Thread.sleep(timeUnit.toMillis(time));
              } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
              }
              task.run();
            }));
  }

  static class BungeeTimeoutTask implements TimeoutTask {

    private final Future<?> task;

    public BungeeTimeoutTask(Future<?> task) {
      this.task = task;
    }

    @Override
    public boolean hasCalled() {
      return task.isDone();
    }

    @Override
    public void cancel() {
      task.cancel(true);
    }
  }
}

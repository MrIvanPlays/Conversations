package com.mrivanplays.conversations.velocity;

import com.mrivanplays.conversations.base.timeout.TimeoutScheduler;
import com.mrivanplays.conversations.base.timeout.TimeoutTask;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.scheduler.ScheduledTask;
import com.velocitypowered.api.scheduler.TaskStatus;
import java.util.concurrent.TimeUnit;

class VelocityTimeoutScheduler implements TimeoutScheduler {

  private final ProxyServer proxy;
  private final Object plugin;

  VelocityTimeoutScheduler(Object plugin, ProxyServer proxy) {
    this.plugin = plugin;
    this.proxy = proxy;
  }

  @Override
  public TimeoutTask schedule(Runnable task, long time, TimeUnit timeUnit) {
    return new VelocityTimeoutTask(
        proxy.getScheduler().buildTask(plugin, task).delay(time, timeUnit).schedule());
  }

  static class VelocityTimeoutTask implements TimeoutTask {

    private final ScheduledTask task;

    public VelocityTimeoutTask(ScheduledTask task) {
      this.task = task;
    }

    @Override
    public boolean hasCalled() {
      return task.status() == TaskStatus.FINISHED;
    }

    @Override
    public void cancel() {
      task.cancel();
    }
  }
}

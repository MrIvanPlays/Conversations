package com.mrivanplays.conversations.spigot;

import com.mrivanplays.conversations.base.timeout.TimeoutScheduler;
import com.mrivanplays.conversations.base.timeout.TimeoutTask;
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

/**
 * {@inheritDoc}
 */
public class BukkitTimeoutScheduler implements TimeoutScheduler {

  private final Plugin plugin;

  public BukkitTimeoutScheduler(Plugin plugin) {
    this.plugin = plugin;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TimeoutTask schedule(Runnable task, int time, TimeUnit timeUnit) {
    return new BukkitTimeoutTask(
        plugin
            .getServer()
            .getScheduler()
            .runTaskLater(plugin, task, timeUnit.toSeconds(time) * 20));
  }

  private static class BukkitTimeoutTask implements TimeoutTask {

    private final BukkitTask bukkitTask;

    public BukkitTimeoutTask(BukkitTask bukkitTask) {
      this.bukkitTask = bukkitTask;
    }

    @Override
    public boolean hasCalled() {
      return !Bukkit.getScheduler().isCurrentlyRunning(bukkitTask.getTaskId())
          && !Bukkit.getScheduler().isQueued(bukkitTask.getTaskId());
    }

    @Override
    public void cancel() {
      bukkitTask.cancel();
    }
  }
}

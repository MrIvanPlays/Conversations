package com.mrivanplays.conversations.paper;

import com.mrivanplays.conversations.base.Conversation;
import com.mrivanplays.conversations.base.ConversationContext.EndState;
import com.mrivanplays.conversations.base.ConversationManager;
import com.mrivanplays.conversations.base.timeout.TimeoutScheduler;
import com.mrivanplays.conversations.spigot.BukkitTimeoutScheduler;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Represents a conversation manager for the paper minecraft server software.
 *
 * @author MrIvanPlays
 */
public class PaperConversationManager
    extends ConversationManager<Component, PaperConversationPartner> {

  private final Map<UUID, PaperConversationPartner> partners = new HashMap<>();
  private final TimeoutScheduler timeoutScheduler;

  public PaperConversationManager(Plugin plugin) {
    Objects.requireNonNull(plugin, "plugin");
    plugin.getServer().getPluginManager().registerEvents(new PaperConvoListener(this), plugin);
    timeoutScheduler = new BukkitTimeoutScheduler(plugin);
  }

  void removePartner(UUID uuid) {
    partners.remove(uuid);
  }

  /**
   * Returns the {@link TimeoutScheduler} for this conversation manager.
   *
   * @return timeout scheduler
   */
  public TimeoutScheduler getTimeoutScheduler() {
    return timeoutScheduler;
  }

  /**
   * Returns a new {@link Conversation.Builder} populated with the specified {@link Player} {@code
   * player} as a conversation partner, with this manager as a parent manager and with this
   * manager's {@link #getTimeoutScheduler()}
   *
   * @param player conversation partner player
   * @return conversation builder
   */
  public Conversation.Builder<Component, PaperConversationPartner> newConversationBuilder(
      Player player) {
    Objects.requireNonNull(player, "player");
    return Conversation.<Component, PaperConversationPartner>newBuilder()
        .parentManager(this)
        .withTimeoutScheduler(timeoutScheduler)
        .withConversationPartner(
            partners.computeIfAbsent(
                player.getUniqueId(), $ -> new PaperConversationPartner(player)));
  }

  @Override
  protected void unregisterConversation(UUID conversationPartner, EndState endState) {
    super.unregisterConversation(conversationPartner, endState);
  }
}

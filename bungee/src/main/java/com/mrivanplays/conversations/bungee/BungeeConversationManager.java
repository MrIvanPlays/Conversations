package com.mrivanplays.conversations.bungee;

import com.mrivanplays.conversations.base.Conversation;
import com.mrivanplays.conversations.base.ConversationContext.EndState;
import com.mrivanplays.conversations.base.ConversationManager;
import com.mrivanplays.conversations.base.timeout.TimeoutScheduler;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * Represents a conversation manager for the bungeecord proxy.
 *
 * @author MrIvanPlays
 */
public class BungeeConversationManager
    extends ConversationManager<BaseComponent[], BungeeConversationPartner> {

  private final Map<UUID, BungeeConversationPartner> partners = new HashMap<>();
  private final TimeoutScheduler timeoutScheduler;

  public BungeeConversationManager(Plugin plugin) {
    Objects.requireNonNull(plugin, "plugin");
    plugin.getProxy().getPluginManager().registerListener(plugin, new BungeeConvoListener(this));
    timeoutScheduler = new BungeeTimeoutScheduler(plugin);
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
   * Returns a new {@link Conversation.Builder} populated with the specified {@link ProxiedPlayer}
   * {@code player} as a conversation partner, with this manager as a parent manager and with this
   * manager's {@link #getTimeoutScheduler()}
   *
   * @param player conversation partner player
   * @return conversation builder
   */
  public Conversation.Builder<BaseComponent[], BungeeConversationPartner> newConversationBuilder(
      ProxiedPlayer player) {
    Objects.requireNonNull(player, "player");
    return Conversation.<BaseComponent[], BungeeConversationPartner>newBuilder()
        .parentManager(this)
        .withTimeoutScheduler(timeoutScheduler)
        .withConversationPartner(
            partners.computeIfAbsent(
                player.getUniqueId(), $ -> new BungeeConversationPartner(player)));
  }

  @Override
  protected void unregisterConversation(UUID conversationPartner, EndState endState) {
    super.unregisterConversation(conversationPartner, endState);
  }
}

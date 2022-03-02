package com.mrivanplays.conversations.velocity;

import com.mrivanplays.conversations.base.Conversation;
import com.mrivanplays.conversations.base.ConversationContext.EndState;
import com.mrivanplays.conversations.base.ConversationManager;
import com.mrivanplays.conversations.base.timeout.TimeoutScheduler;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import net.kyori.adventure.text.Component;

/**
 * Represents a conversation manager for the velocity proxy.
 *
 * @author MrIvanPlays
 */
public class VelocityConversationManager
    extends ConversationManager<Component, VelocityConversationPartner> {

  private final Map<UUID, VelocityConversationPartner> partners = new HashMap<>();
  private final TimeoutScheduler timeoutScheduler;

  public VelocityConversationManager(Object plugin, ProxyServer proxy) {
    Objects.requireNonNull(plugin, "plugin");
    Objects.requireNonNull(proxy, "proxy");
    proxy.getEventManager().register(plugin, new VelocityConvoListener(this));
    timeoutScheduler = new VelocityTimeoutScheduler(plugin, proxy);
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
  public Conversation.Builder<Component, VelocityConversationPartner> newConversationBuilder(
      Player player) {
    Objects.requireNonNull(player, "player");
    return Conversation.<Component, VelocityConversationPartner>newBuilder()
        .parentManager(this)
        .withTimeoutScheduler(timeoutScheduler)
        .withConversationPartner(
            partners.computeIfAbsent(
                player.getUniqueId(), $ -> new VelocityConversationPartner(player)));
  }

  @Override
  protected void unregisterConversation(UUID conversationPartner, EndState endState) {
    super.unregisterConversation(conversationPartner, endState);
  }
}

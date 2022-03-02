package com.mrivanplays.conversations.bungee;

import com.mrivanplays.conversations.base.ConversationPartner;
import java.util.UUID;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * Represents a conversation partner for bungeecord proxy.
 *
 * @author MrIvanPlays
 */
public class BungeeConversationPartner implements ConversationPartner<BaseComponent[]> {

  private final ProxiedPlayer player;

  BungeeConversationPartner(ProxiedPlayer player) {
    this.player = player;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UUID getUniqueIdentifier() {
    return player.getUniqueId();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendMessage(BaseComponent... message) {
    player.sendMessage(message);
  }

  /**
   * Returns the wrapped {@link ProxiedPlayer} object.
   *
   * @return wrapped player
   */
  public ProxiedPlayer getPlayer() {
    return player;
  }
}

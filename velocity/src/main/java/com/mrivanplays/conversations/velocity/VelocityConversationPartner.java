package com.mrivanplays.conversations.velocity;

import com.mrivanplays.conversations.base.ConversationPartner;
import com.velocitypowered.api.proxy.Player;
import java.util.UUID;
import net.kyori.adventure.text.Component;

/**
 * Represents a conversation partner for velocity proxy.
 *
 * @author MrIvanPlays
 */
public class VelocityConversationPartner implements ConversationPartner<Component> {

  private final Player player;

  VelocityConversationPartner(Player player) {
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
  public void sendMessage(Component message) {
    player.sendMessage(message);
  }

  /**
   * Returns the wrapped {@link Player} object.
   *
   * @return wrapped player
   */
  public Player getPlayer() {
    return player;
  }
}

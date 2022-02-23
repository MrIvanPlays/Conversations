package com.mrivanplays.conversations.spigot;

import com.mrivanplays.conversations.base.ConversationPartner;
import java.util.UUID;
import org.bukkit.entity.Player;

/**
 * Represents a conversation partner for bukkit minecraft server software.
 *
 * @author MrIvanPlays
 */
public class BukkitConversationPartner implements ConversationPartner<String> {

  private final Player player;

  BukkitConversationPartner(Player player) {
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
  public void sendMessage(String message) {
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

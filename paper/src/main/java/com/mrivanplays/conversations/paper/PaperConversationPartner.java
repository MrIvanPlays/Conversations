package com.mrivanplays.conversations.paper;

import com.mrivanplays.conversations.base.ConversationPartner;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

/**
 * Represents a conversation partner for paper minecraft server software.
 *
 * @author MrIvanPlays
 */
public class PaperConversationPartner implements ConversationPartner<Component> {

  private final Player player;

  PaperConversationPartner(Player player) {
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

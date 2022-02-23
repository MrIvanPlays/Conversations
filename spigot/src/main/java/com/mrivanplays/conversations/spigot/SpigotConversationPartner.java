package com.mrivanplays.conversations.spigot;

import com.mrivanplays.conversations.base.ConversationPartner;
import java.util.UUID;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;

/**
 * Represents a conversation partner for bukkit minecraft server software.
 *
 * @author MrIvanPlays
 */
public class SpigotConversationPartner implements ConversationPartner<BaseComponent[]> {

  private final Player player;

  SpigotConversationPartner(Player player) {
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
    player.spigot().sendMessage(message);
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

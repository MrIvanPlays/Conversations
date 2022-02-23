package com.mrivanplays.conversations.spigot;

import com.mrivanplays.conversations.base.ConversationContext.EndState;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

class SpigotConvoListener implements Listener {

  private final SpigotConversationManager convoManager;

  public SpigotConvoListener(SpigotConversationManager convoManager) {
    this.convoManager = convoManager;
  }

  @EventHandler
  public void onChat(AsyncPlayerChatEvent event) {
    Player player = event.getPlayer();
    if (!convoManager.hasActiveConversation(player.getUniqueId())) {
      return;
    }
    event.setCancelled(true);
    convoManager.acceptInput(
        player.getUniqueId(), TextComponent.fromLegacyText(event.getMessage()));
  }

  @EventHandler
  public void onQuit(PlayerQuitEvent event) {
    convoManager.removePartner(event.getPlayer().getUniqueId());
    convoManager.unregisterConversation(
        event.getPlayer().getUniqueId(), EndState.PARTNER_DISCONNECT);
  }
}

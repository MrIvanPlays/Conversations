package com.mrivanplays.conversations.spigot;

import com.mrivanplays.conversations.base.ConversationContext.EndState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

class BukkitConvoListener implements Listener {

  private final BukkitConversationManager convoManager;

  BukkitConvoListener(BukkitConversationManager manager) {
    this.convoManager = manager;
  }

  @EventHandler
  public void onChat(AsyncPlayerChatEvent event) {
    Player player = event.getPlayer();
    if (!convoManager.hasActiveConversation(player.getUniqueId())) {
      return;
    }
    event.setCancelled(true);
    convoManager.acceptInput(player.getUniqueId(), event.getMessage());
  }

  @EventHandler
  public void onQuit(PlayerQuitEvent event) {
    convoManager.removePartner(event.getPlayer().getUniqueId());
    convoManager.unregisterConversation(
        event.getPlayer().getUniqueId(), EndState.PARTNER_DISCONNECT);
  }
}

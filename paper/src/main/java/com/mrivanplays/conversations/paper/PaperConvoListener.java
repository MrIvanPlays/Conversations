package com.mrivanplays.conversations.paper;

import com.mrivanplays.conversations.base.ConversationContext.EndState;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

class PaperConvoListener implements Listener {

  private final PaperConversationManager convoManager;

  public PaperConvoListener(PaperConversationManager convoManager) {
    this.convoManager = convoManager;
  }

  @EventHandler
  public void onChat(AsyncChatEvent event) {
    Player player = event.getPlayer();
    if (!convoManager.hasActiveConversation(player.getUniqueId())) {
      return;
    }
    event.setCancelled(true);
    convoManager.acceptInput(player.getUniqueId(), event.message());
  }

  @EventHandler
  public void onQuit(PlayerQuitEvent event) {
    convoManager.removePartner(event.getPlayer().getUniqueId());
    convoManager.unregisterConversation(
        event.getPlayer().getUniqueId(), EndState.PARTNER_DISCONNECT);
  }
}

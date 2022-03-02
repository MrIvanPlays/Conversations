package com.mrivanplays.conversations.bungee;

import com.mrivanplays.conversations.base.ConversationContext.EndState;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

class BungeeConvoListener implements Listener {

  private final BungeeConversationManager convoManager;

  public BungeeConvoListener(BungeeConversationManager convoManager) {
    this.convoManager = convoManager;
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onChat(ChatEvent event) {
    if (event.isCommand()) {
      return;
    }

    ProxiedPlayer player = (ProxiedPlayer) event.getSender();
    if (!convoManager.hasActiveConversation(player.getUniqueId())) {
      return;
    }
    event.setCancelled(true);
    convoManager.acceptInput(
        player.getUniqueId(), TextComponent.fromLegacyText(event.getMessage()));
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onDisconnect(PlayerDisconnectEvent event) {
    convoManager.removePartner(event.getPlayer().getUniqueId());
    convoManager.unregisterConversation(
        event.getPlayer().getUniqueId(), EndState.PARTNER_DISCONNECT);
  }
}

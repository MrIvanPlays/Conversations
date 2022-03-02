package com.mrivanplays.conversations.velocity;

import com.mrivanplays.conversations.base.ConversationContext.EndState;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent.ChatResult;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

class VelocityConvoListener {

  private final VelocityConversationManager convoManager;

  VelocityConvoListener(VelocityConversationManager convoManager) {
    this.convoManager = convoManager;
  }

  @Subscribe
  public void onChat(PlayerChatEvent event) {
    Player player = event.getPlayer();
    if (!convoManager.hasActiveConversation(player.getUniqueId())) {
      return;
    }
    event.setResult(ChatResult.denied());
    convoManager.acceptInput(
        player.getUniqueId(),
        LegacyComponentSerializer.legacyAmpersand().deserialize(event.getMessage()));
  }

  @Subscribe
  public void onDisconnect(DisconnectEvent event) {
    convoManager.removePartner(event.getPlayer().getUniqueId());
    convoManager.unregisterConversation(
        event.getPlayer().getUniqueId(), EndState.PARTNER_DISCONNECT);
  }
}

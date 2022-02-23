package com.mrivanplays.conversations.base;

import java.util.UUID;

/**
 * Represents a conversation partner
 *
 * @author MrIvanPlays
 */
public interface ConversationPartner<MessageType> {

  /**
   * Returns the unique identifier {@link UUID} of this conversation partner.
   *
   * @return uuid
   */
  UUID getUniqueIdentifier();

  /**
   * Sends the specified {@code message} to this conversation partner.
   *
   * @param message message to send
   */
  void sendMessage(MessageType message);

}

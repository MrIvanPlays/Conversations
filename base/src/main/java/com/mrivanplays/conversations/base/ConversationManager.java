package com.mrivanplays.conversations.base;

import com.mrivanplays.conversations.base.ConversationContext.EndState;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a manager of {@link Conversation conversations}
 *
 * @author MrIvanPlays
 */
public class ConversationManager<MessageType, SenderType extends ConversationPartner<MessageType>> {

  private final Map<UUID, Conversation<MessageType, SenderType>> conversations = new HashMap<>();

  /**
   * Registers a new {@link Conversation}. In 99% of the use cases you wouldn't need to use this
   * method.
   *
   * @param conversation conversation to register
   * @throws IllegalArgumentException if the conversation partner of the specified conversation is
   *     already into a conversation
   */
  public void registerConversation(Conversation<MessageType, SenderType> conversation)
      throws IllegalArgumentException {
    Objects.requireNonNull(conversation, "conversation");
    if (conversations.containsKey(conversation.getConversationPartner().getUniqueIdentifier())) {
      Conversation<MessageType, SenderType> convo =
          conversations.get(conversation.getConversationPartner().getUniqueIdentifier());
      if (convo != null && !convo.hasEnded()) {
        throw new IllegalArgumentException("Cannot have 2 conversations at the same time.");
      } else if (convo == null) {
        conversations.remove(conversation.getConversationPartner().getUniqueIdentifier());
      } else {
        conversations.remove(conversation.getConversationPartner().getUniqueIdentifier());
      }
    }
    conversations.put(conversation.getConversationPartner().getUniqueIdentifier(), conversation);
  }

  /**
   * Unregisters the {@link Conversation} of the specified {@link UUID} {@code conversationPartner}.
   * In 99% of the use cases you wouldn't need to use this method.
   *
   * @param conversationPartner conversation partner unique identifier
   */
  public void unregisterConversation(UUID conversationPartner) {
    Objects.requireNonNull(conversationPartner, "conversationPartner");
    unregisterConversation(conversationPartner, EndState.UNKNOWN);
  }

  protected void unregisterConversation(UUID conversationPartner, EndState endState) {
    Conversation<MessageType, SenderType> conversation = conversations.get(conversationPartner);
    if (conversation == null) {
      return;
    }
    if (conversation.hasEnded()) {
      conversations.remove(conversationPartner);
      return;
    }
    conversation.callDoneState(endState);
    conversations.remove(conversationPartner);
  }

  /**
   * Returns whether the specified {@link UUID} {@code conversationPartner} has an ongoing {@link
   * Conversation}.
   *
   * @param conversationPartner conversation partner unique id
   * @return whether in conversation
   */
  public boolean hasActiveConversation(UUID conversationPartner) {
    Objects.requireNonNull(conversationPartner, "conversationPartner");
    if (conversations.containsKey(conversationPartner)) {
      Conversation<MessageType, SenderType> convo = conversations.get(conversationPartner);
      if (!convo.hasEnded()) {
        return true;
      } else {
        unregisterConversation(conversationPartner);
        return false;
      }
    } else {
      return false;
    }
  }

  /**
   * Handles the specified {@code message} input for the specified {@link UUID} {@code
   * conversationPartner}'s {@link Conversation} if any.
   *
   * @param conversationPartner conversation partner
   * @param message message
   */
  public void acceptInput(UUID conversationPartner, MessageType message) {
    Objects.requireNonNull(conversationPartner, "conversationPartner");
    Objects.requireNonNull(message, "message");
    Conversation<MessageType, SenderType> conversation = conversations.get(conversationPartner);
    if (conversation != null) {
      if (!conversation.hasEnded()) {
        conversation.acceptInput(message);
      } else {
        unregisterConversation(conversationPartner);
      }
    }
  }
}

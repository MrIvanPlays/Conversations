package com.mrivanplays.conversations.base;

import com.mrivanplays.conversations.base.question.Question;
import java.util.Map;

/**
 * Represents a ended {@link Conversation}'s context.
 *
 * @author MrIvanPlays
 */
public final class ConversationContext<
    MessageType, SenderType extends ConversationPartner<MessageType>> {

  /**
   * Create a new {@link ConversationContext}
   *
   * @param conversationPartner conversation partner
   * @param inputs question answers
   * @param endState how did the conversation end
   * @param questionIdentifierEndedOn the question on which the conversation ended by a timeout on.
   *     could be null
   * @param <MessageType> message type generic
   * @param <SenderType> sender type (conversation partner type) generic
   * @return new context
   */
  public static <MessageType, SenderType extends ConversationPartner<MessageType>>
      ConversationContext<MessageType, SenderType> of(
          SenderType conversationPartner,
          Map<String, MessageType> inputs,
          EndState endState,
          String questionIdentifierEndedOn) {
    return new ConversationContext<>(
        conversationPartner, inputs, endState, questionIdentifierEndedOn);
  }

  private final SenderType conversationPartner;
  private final EndState endState;
  private final String questionIdentifierEndedOn;
  private final Map<String, MessageType> inputs;

  private ConversationContext(
      SenderType conversationPartner,
      Map<String, MessageType> inputs,
      EndState endState,
      String questionIdentifierEndedOn) {
    this.conversationPartner = conversationPartner;
    this.endState = endState;
    this.inputs = inputs;
    this.questionIdentifierEndedOn = questionIdentifierEndedOn;
  }

  /**
   * Returns the conversation partner.
   *
   * @return conversation partner
   */
  public SenderType getConversationPartner() {
    return conversationPartner;
  }

  /**
   * Returns the answer of the {@link Question}, matching the specified {@code questionIdentifier}
   *
   * @param questionIdentifier question identifier
   * @return message answer. could be null depending on the question position and whether a previous
   *     question (or this question) has their timeout triggered.
   */
  public MessageType getInput(String questionIdentifier) {
    return inputs.get(questionIdentifier);
  }

  /**
   * Returns the state of how this context was created.
   *
   * @return end state
   */
  public EndState getEndState() {
    return endState;
  }

  /**
   * Returns the question identifier on which the {@link Conversation} this context originated from
   * ended. This is null always when {@link #getEndState()} is {@link EndState#SUCCESS}. If not
   * null, the chances of {@link #getInput(String)} called with this question identifier to return
   * null are pretty high.
   *
   * @return question identifier the conversation ended on
   */
  public String getQuestionIdentifierEndedOn() {
    return questionIdentifierEndedOn;
  }

  /**
   * Represents an enum of states of how the context used in was created.
   *
   * @author MrIvanPlays
   */
  public enum EndState {
    /** Indicates a successful ending state */
    SUCCESS,
    /** Indicates a timeout has occurred */
    TIMEOUT,
    /** Indicates that the conversation partner has disconnected. */
    PARTNER_DISCONNECT,
    /** Indicates that the conversation was ended by an unknown reason. */
    UNKNOWN
  }
}

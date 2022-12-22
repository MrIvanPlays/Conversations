package com.mrivanplays.conversations.base.question;

import com.mrivanplays.conversations.base.ConversationPartner;
import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Queue;
import java.util.function.Function;

/**
 * Represents a chained question.
 *
 * <p>A chained question is a series of {@link Question questions}, the first being immutable whilst
 * the next questions can vary based on the {@link ConversationPartner conversation partner's}
 * input.
 *
 * @param <MessageType> message object type
 * @param <SenderType> conversation partner type
 * @see ComputeContext
 */
public final class ChainedQuestion<
    MessageType, SenderType extends ConversationPartner<MessageType>> {

  private final Question<MessageType, SenderType> firstQuestion;
  private Queue<
          Function<ComputeContext<MessageType, SenderType>, Question<MessageType, SenderType>>>
      questionsFunc = new ArrayDeque<>();

  public ChainedQuestion(Question<MessageType, SenderType> firstQuestion) {
    this.firstQuestion = Objects.requireNonNull(firstQuestion, "firstQuestion");
  }

  /**
   * Returns the first {@link Question}, or the question that a {@link
   * com.mrivanplays.conversations.base.Conversation} can start with.
   *
   * @return first question
   */
  public Question<MessageType, SenderType> getFirstQuestion() {
    return this.firstQuestion;
  }

  /**
   * Computes the next question with the specified {@link ComputeContext compute context}. If there
   * is no next question, this returns null.
   *
   * @param context compute context
   * @return next question or null
   * @see ComputeContext
   */
  public Question<MessageType, SenderType> getNextQuestion(
      ComputeContext<MessageType, SenderType> context) {
    Objects.requireNonNull(context, "context");
    if (this.questionsFunc.isEmpty()) {
      return null;
    }
    return this.questionsFunc.poll().apply(context);
  }

  /**
   * Add a compute method for a question. The questions are ordered as FIFO e.g. if you have
   *
   * <pre>
   *   <code>
   * .addQuestion((context) -> Question.of("id1", "My question"))
   *     .addQuestion((context) -> Question.of("id2", "My other question"))
   *   </code>
   * </pre>
   *
   * the question "My question" will be asked first and "My other question" will be asked next.
   *
   * @param func question compute {@link Function}
   * @return this instance for chaining
   */
  public ChainedQuestion<MessageType, SenderType> addQuestion(
      Function<ComputeContext<MessageType, SenderType>, Question<MessageType, SenderType>> func) {
    Objects.requireNonNull(func, "func");
    this.questionsFunc.offer(func);
    return this;
  }

  /**
   * Represents a context, with which a {@link Question} can be computed.
   *
   * @param <MessageType> message object type
   * @param <SenderType> conversation partner type
   */
  public static final class ComputeContext<
      MessageType, SenderType extends ConversationPartner<MessageType>> {

    private final Question<MessageType, SenderType> previousQuestion;
    private final MessageType messageInput;
    private final SenderType conversationPartner;

    /**
     * Construct a new compute context.
     *
     * @param previousQuestion the previous question asked. Cannot be null
     * @param messageInput the input message for the previous question. Cannot be null.
     * @param conversationPartner the conversation partner. Cannot be null
     */
    public ComputeContext(
        Question<MessageType, SenderType> previousQuestion,
        MessageType messageInput,
        SenderType conversationPartner) {
      this.previousQuestion = Objects.requireNonNull(previousQuestion, "previousQuestion");
      this.messageInput = Objects.requireNonNull(messageInput, "messageInput");
      this.conversationPartner = Objects.requireNonNull(conversationPartner, "conversationPartner");
    }

    /**
     * Returns the previous asked question.
     *
     * @return previous question
     */
    public Question<MessageType, SenderType> getPreviousQuestion() {
      return this.previousQuestion;
    }

    /**
     * Returns the input message. This is the message answer of the {@link #getPreviousQuestion()}
     *
     * @return input message
     */
    public MessageType getInputMessage() {
      return this.messageInput;
    }

    /**
     * Returns the {@link ConversationPartner}
     *
     * @return conversation partner
     */
    public SenderType getConversationPartner() {
      return this.conversationPartner;
    }
  }
}

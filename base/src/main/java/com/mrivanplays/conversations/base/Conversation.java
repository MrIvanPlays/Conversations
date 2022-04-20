package com.mrivanplays.conversations.base;

import com.mrivanplays.conversations.base.ConversationContext.EndState;
import com.mrivanplays.conversations.base.question.InputValidator;
import com.mrivanplays.conversations.base.question.Question;
import com.mrivanplays.conversations.base.timeout.TimeoutScheduler;
import com.mrivanplays.conversations.base.timeout.TimeoutTask;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Represents a conversation.
 *
 * @author MrIvanPlays
 * @see ConversationPartner
 */
public final class Conversation<MessageType, SenderType extends ConversationPartner<MessageType>> {

  /**
   * Creates a new {@link Conversation.Builder}
   *
   * @param <MessageType> message type generic
   * @param <SenderType> sender type (partner type) generic
   * @return builder
   */
  public static <MessageType, SenderType extends ConversationPartner<MessageType>>
      Builder<MessageType, SenderType> newBuilder() {
    return new Builder<>();
  }

  public static <MessageType, SenderType extends ConversationPartner<MessageType>>
      Builder<MessageType, SenderType> newBuilder(Conversation<MessageType, SenderType> copy) {
    Builder<MessageType, SenderType> builder = new Builder<>();
    builder.conversationPartner = copy.getConversationPartner();
    builder.questions = copy.questions;
    builder.doneHandler = copy.doneHandler;
    builder.timeoutScheduler = copy.timeoutScheduler;
    builder.conversationManager = copy.conversationManager;
    return builder;
  }

  private final ConversationManager<MessageType, SenderType> conversationManager;
  private final SenderType conversationPartner;
  private final TimeoutScheduler timeoutScheduler;
  private final List<Question<MessageType, SenderType>> questions;
  private final Consumer<ConversationContext<MessageType, SenderType>> doneHandler;
  private final Map<String, MessageType> inputs = new LinkedHashMap<>();

  private boolean ended = false;

  private int currentQuestionIndex;
  private TimeoutTask lastTimeoutTask;

  private Conversation(Builder<MessageType, SenderType> builder) {
    conversationManager =
        Objects.requireNonNull(builder.conversationManager, "conversationManager");
    conversationPartner =
        Objects.requireNonNull(builder.conversationPartner, "conversationPartner");
    doneHandler = Objects.requireNonNull(builder.doneHandler, "doneHandler");
    List<Question<MessageType, SenderType>> builderQuestions = builder.questions;
    if (builderQuestions.isEmpty()) {
      throw new IllegalArgumentException("No questions specified");
    }
    this.questions = builderQuestions;
    this.timeoutScheduler = builder.timeoutScheduler;
  }

  /** Starts the conversation by sending the first question. */
  public void start() {
    currentQuestionIndex = 0;
    conversationManager.registerConversation(this);
    handleQuestion(questions.get(currentQuestionIndex));
  }

  /**
   * Returns whether this conversation has ended.
   *
   * @return ended state
   */
  public boolean hasEnded() {
    return ended;
  }

  /**
   * Returns the conversation partner of this conversation.
   *
   * @return conversation partner
   */
  public SenderType getConversationPartner() {
    return conversationPartner;
  }

  /**
   * Handles the specified {@code message} input.
   *
   * @param message input to handle
   */
  public void acceptInput(MessageType message) {
    if (lastTimeoutTask != null) {
      if (lastTimeoutTask.hasCalled()) {
        ended = true;
        conversationManager.unregisterConversation(conversationPartner.getUniqueIdentifier());
      } else {
        lastTimeoutTask.cancel();
      }
    }
    if (ended) {
      return;
    }
    Question<MessageType, SenderType> question = questions.get(currentQuestionIndex);
    if (question.getInputValidator() != null) {
      InputValidator.ValidationResult<MessageType> validationResult =
          question.getInputValidator().validate(message);
      if (!validationResult.isSuccessful()) {
        conversationPartner.sendMessage(validationResult.getErrorMessage());
        if (validationResult.shallAskQuestionAgain()) {
          conversationPartner.sendMessage(question.getMessage());
        } else if (validationResult.shallCallDoneState()) {
          ended = true;
          conversationManager.unregisterConversation(conversationPartner.getUniqueIdentifier());
          doneHandler.accept(
              ConversationContext.of(
                  conversationPartner,
                  inputs,
                  EndState.INPUT_VALIDATION_HANDLER_FAIL,
                  question.getIdentifier()));
        }
        return;
      }
    }
    inputs.put(question.getIdentifier(), message);
    if ((currentQuestionIndex + 1) == questions.size()) {
      ended = true;
      conversationManager.unregisterConversation(conversationPartner.getUniqueIdentifier());
      doneHandler.accept(
          ConversationContext.of(conversationPartner, inputs, EndState.SUCCESS, null));
    } else {
      currentQuestionIndex++;
      handleQuestion(questions.get(currentQuestionIndex));
    }
  }

  private void handleQuestion(Question<MessageType, SenderType> question) {
    conversationPartner.sendMessage(question.getMessage());
    if (question.getTimeout() != -1 && timeoutScheduler != null) {
      lastTimeoutTask =
          timeoutScheduler.schedule(
              () -> {
                doneHandler.accept(
                    ConversationContext.of(
                        conversationPartner, inputs, EndState.TIMEOUT, question.getIdentifier()));
                if (question.getWhenTimeout() != null) {
                  question.getWhenTimeout().accept(conversationPartner);
                }
              },
              question.getTimeout(),
              question.getTimeoutUnit());
    }
  }

  void callDoneState(EndState endState) {
    if (ended) {
      return;
    }
    ended = true;
    Question<MessageType, SenderType> question = questions.get(currentQuestionIndex);
    doneHandler.accept(
        ConversationContext.of(conversationPartner, inputs, endState, question.getIdentifier()));
  }

  /**
   * Represents a builder of {@link Conversation}
   *
   * @author MrIvanPlays
   */
  public static final class Builder<
      MessageType, SenderType extends ConversationPartner<MessageType>> {

    private SenderType conversationPartner;
    private ConversationManager<MessageType, SenderType> conversationManager;
    private TimeoutScheduler timeoutScheduler;
    private Consumer<ConversationContext<MessageType, SenderType>> doneHandler;
    private List<Question<MessageType, SenderType>> questions = new LinkedList<>();

    private Builder() {}

    /**
     * Specify the parent {@link ConversationManager} of the built conversation.
     *
     * @param conversationManager parent manager
     * @return this instance for chaining
     * @see ConversationManager
     */
    public Builder<MessageType, SenderType> parentManager(
        ConversationManager<MessageType, SenderType> conversationManager) {
      this.conversationManager = conversationManager;
      return this;
    }

    /**
     * Specify the handler when this conversation ends.
     *
     * @param doneHandler done handler
     * @return this instance for chaining
     * @see ConversationContext
     */
    public Builder<MessageType, SenderType> whenDone(
        Consumer<ConversationContext<MessageType, SenderType>> doneHandler) {
      this.doneHandler = doneHandler;
      return this;
    }

    /**
     * Specify the conversation partner of the built conversation.
     *
     * @param val partner
     * @return this instance for chaining
     */
    public Builder<MessageType, SenderType> withConversationPartner(SenderType val) {
      conversationPartner = val;
      return this;
    }

    /**
     * Specify the {@link TimeoutScheduler} of the build conversation. Used to schedule {@link
     * Question} timeouts.
     *
     * @param scheduler timeout scheduler
     * @return this instance for chaining
     * @see TimeoutScheduler
     */
    public Builder<MessageType, SenderType> withTimeoutScheduler(TimeoutScheduler scheduler) {
      this.timeoutScheduler = scheduler;
      return this;
    }

    /**
     * Specify a {@link Question} to be sent to the conversation partner.
     *
     * @param val question
     * @return this instance for chaining
     * @see Question
     */
    public Builder<MessageType, SenderType> withQuestion(Question<MessageType, SenderType> val) {
      questions.add(val);
      return this;
    }

    /**
     * Builds this builder into a {@link Conversation}
     *
     * @return conversation
     */
    public Conversation<MessageType, SenderType> build() {
      return new Conversation<>(this);
    }
  }
}

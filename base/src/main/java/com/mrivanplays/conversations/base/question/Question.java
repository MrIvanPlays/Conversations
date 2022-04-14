package com.mrivanplays.conversations.base.question;

import com.mrivanplays.conversations.base.ConversationPartner;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Represents a question, which would be asked to a {@link ConversationPartner}.
 *
 * @author MrIvanPlays
 */
public final class Question<MessageType, SenderType extends ConversationPartner<MessageType>> {

  /**
   * Creates a simple question, consisting of its {@code identifier} and {@code message}.
   *
   * @param identifier question identifier
   * @param message message question
   * @param <MessageType> message type generic
   * @param <SenderType> sender type (partner type) generic
   * @return question
   */
  public static <MessageType, SenderType extends ConversationPartner<MessageType>>
      Question<MessageType, SenderType> of(String identifier, MessageType message) {
    return new Builder<MessageType, SenderType>()
        .withIdentifier(identifier)
        .withMessage(message)
        .build();
  }

  /**
   * Creates a new {@link Question.Builder}
   *
   * @param <MessageType> message type generic
   * @param <SenderType> sender type (partner type) generic
   * @return question builder
   */
  public static <MessageType, SenderType extends ConversationPartner<MessageType>>
      Builder<MessageType, SenderType> newBuilder() {
    return new Builder<>();
  }

  public static <MessageType, SenderType extends ConversationPartner<MessageType>>
      Builder<MessageType, SenderType> newBuilder(Question<MessageType, SenderType> copy) {
    Builder<MessageType, SenderType> builder = new Builder<>();
    builder.identifier = copy.getIdentifier();
    builder.message = copy.getMessage();
    builder.timeout = copy.getTimeout();
    builder.timeoutUnit = copy.getTimeoutUnit();
    builder.whenTimeout = copy.getWhenTimeout();
    builder.inputValidator = copy.getInputValidator();
    return builder;
  }

  private final String identifier;
  private final MessageType message;
  private long timeout;
  private TimeUnit timeoutUnit;
  private Consumer<SenderType> whenTimeout;
  private InputValidator<MessageType> inputValidator;

  private Question(Builder<MessageType, SenderType> builder) {
    identifier = Objects.requireNonNull(builder.identifier, "identifier");
    message = Objects.requireNonNull(builder.message, "message");
    timeout = builder.timeout;
    timeoutUnit = timeout != -1 ? Objects.requireNonNull(builder.timeoutUnit, "timeoutUnit") : null;
    whenTimeout = builder.whenTimeout;
    inputValidator = builder.inputValidator;
  }

  /**
   * Returns the identifier of this question.
   *
   * @return identifier
   */
  public String getIdentifier() {
    return identifier;
  }

  /**
   * Returns the question message of this question.
   *
   * @return question message
   */
  public MessageType getMessage() {
    return message;
  }

  /**
   * Returns the timeout of this question. Returns -1 if not specified.
   *
   * @return timeout or -1
   */
  public long getTimeout() {
    return timeout;
  }

  /**
   * Returns the timeout {@link TimeUnit} of this question. Returns null if the timeout is not
   * specified. If a timeout is specified, this is always not null.
   *
   * @return timeout unit
   */
  public TimeUnit getTimeoutUnit() {
    return timeoutUnit;
  }

  /**
   * Returns the {@link Consumer} which would be run when the question expires (time out).
   *
   * @return when timeout action
   */
  public Consumer<SenderType> getWhenTimeout() {
    return whenTimeout;
  }

  /**
   * Returns the {@link InputValidator}
   *
   * @return input validator
   * @see InputValidator
   */
  public InputValidator<MessageType> getInputValidator() {
    return inputValidator;
  }

  /**
   * Represents a builder of {@link Question}
   *
   * @author MrIvanPlays
   */
  public static final class Builder<
      MessageType, SenderType extends ConversationPartner<MessageType>> {

    private String identifier;
    private MessageType message;
    private long timeout = -1;
    private TimeUnit timeoutUnit;
    private Consumer<SenderType> whenTimeout;
    private InputValidator<MessageType> inputValidator;

    private Builder() {}

    /**
     * Specify the identifier of the question.
     *
     * @param val identifier
     * @return this instance for chaining
     */
    public Builder<MessageType, SenderType> withIdentifier(String val) {
      identifier = val;
      return this;
    }

    /**
     * Specify the message question of the question.
     *
     * @param val message
     * @return this instance for chaining
     */
    public Builder<MessageType, SenderType> withMessage(MessageType val) {
      message = val;
      return this;
    }

    /**
     * Specify the timeout of this question.
     *
     * @param timeout timeout
     * @param timeUnit timeout unit
     * @return this instance for chaining
     */
    public Builder<MessageType, SenderType> withTimeout(long timeout, TimeUnit timeUnit) {
      this.timeout = timeout;
      this.timeoutUnit = timeUnit;
      return this;
    }

    /**
     * Specify the action to be run when the timeout runs out.
     *
     * @param whenTimeout when timeout action
     * @return this instance for chaining
     */
    public Builder<MessageType, SenderType> whenTimeout(Consumer<SenderType> whenTimeout) {
      this.whenTimeout = whenTimeout;
      return this;
    }

    /**
     * Specify the {@link InputValidator}
     *
     * @param inputValidator input validator
     * @return this instance for chaining
     */
    public Builder<MessageType, SenderType> withInputValidator(
        InputValidator<MessageType> inputValidator) {
      this.inputValidator = inputValidator;
      return this;
    }

    /**
     * Builds this builder into a {@link Question}
     *
     * @return question
     */
    public Question<MessageType, SenderType> build() {
      return new Question<>(this);
    }
  }
}

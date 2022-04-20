package com.mrivanplays.conversations.base.question;

/**
 * Represents a validator of a {@link Question} input.
 *
 * @author MrIvanPlays
 */
@FunctionalInterface
public interface InputValidator<MessageType> {

  /**
   * Validates the {@code message} input and returns a {@link ValidationResult}
   *
   * @param message message to validate
   * @return validation result
   */
  ValidationResult<MessageType> validate(MessageType message);

  /**
   * Represents a validation result. If successful, the error message would be null, if the error
   * message is not null then it's failed.
   *
   * @author MrIvanPlays
   */
  final class ValidationResult<MessageType> {

    /**
     * Returns a single success result.
     *
     * @param <MessageType> message type generic
     * @return success
     */
    public static <MessageType> ValidationResult<MessageType> success() {
      return new ValidationResult<>(null);
    }

    /**
     * Returns a failure result with the specified {@code message}
     *
     * @param message the error message
     * @param <MessageType> message type generic
     * @return validation result considered failure.
     */
    public static <MessageType> ValidationResult<MessageType> fail(MessageType message) {
      return new ValidationResult<>(message);
    }

    /**
     * Returns a failure result with the specified {@code message}
     *
     * @param message the error message
     * @param askQuestionAgain whether to send the question to the conversation partner again.
     * @param <MessageType> message type generic
     * @return validation result
     */
    public static <MessageType> ValidationResult<MessageType> fail(
        MessageType message, boolean askQuestionAgain) {
      return new ValidationResult<>(message, askQuestionAgain);
    }

    /**
     * Returns a failure result with the specified message.
     *
     * @param message the error message
     * @param askQuestionAgain whether to send the question to the conversation partner again
     * @param callDoneState whether to call done state
     * @param <MessageType> message type generic
     * @return validation result
     */
    public static <MessageType> ValidationResult<MessageType> fail(
        MessageType message, boolean askQuestionAgain, boolean callDoneState
    ) {
      return new ValidationResult<>(message, askQuestionAgain, callDoneState);
    }

    private final MessageType errorMessage;
    private final boolean askQuestionAgain;
    private final boolean callDoneState;

    private ValidationResult(MessageType errorMessage) {
      this(errorMessage, false);
    }

    private ValidationResult(MessageType errorMessage, boolean askQuestionAgain) {
      this(errorMessage, askQuestionAgain, false);
    }

    private ValidationResult(MessageType errorMessage, boolean askQuestionAgain, boolean callDoneState) {
      this.errorMessage = errorMessage;
      this.askQuestionAgain = askQuestionAgain;
      this.callDoneState = callDoneState;
    }

    /**
     * Returns whether this validation result is a success.
     *
     * @return success or not
     */
    public boolean isSuccessful() {
      return errorMessage == null && !askQuestionAgain && !callDoneState;
    }

    /**
     * Returns whether we should send the question again to the conversation partner.
     *
     * @return whether to ask again
     */
    public boolean shallAskQuestionAgain() {
      return askQuestionAgain;
    }

    /**
     * Returns whether we shall call the conversation done.
     *
     * @return whether to call done
     */
    public boolean shallCallDoneState() {
      return callDoneState;
    }

    /**
     * Returns the error message, which may be null.
     *
     * @return error message
     */
    public MessageType getErrorMessage() {
      return errorMessage;
    }
  }
}

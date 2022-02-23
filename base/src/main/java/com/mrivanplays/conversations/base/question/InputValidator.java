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

    private final MessageType errorMessage;
    private final boolean askQuestionAgain;

    private ValidationResult(MessageType errorMessage) {
      this(errorMessage, false);
    }

    private ValidationResult(MessageType errorMessage, boolean askQuestionAgain) {
      this.errorMessage = errorMessage;
      this.askQuestionAgain = askQuestionAgain;
    }

    /**
     * Returns whether this validation result is a success.
     *
     * @return success or not
     */
    public boolean isSuccessful() {
      return errorMessage == null && !askQuestionAgain;
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
     * Returns the error message, which may be null.
     *
     * @return error message
     */
    public MessageType getErrorMessage() {
      return errorMessage;
    }
  }
}

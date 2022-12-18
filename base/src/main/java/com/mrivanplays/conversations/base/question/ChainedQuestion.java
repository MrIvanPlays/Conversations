package com.mrivanplays.conversations.base.question;

import com.mrivanplays.conversations.base.ConversationPartner;
import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Queue;
import java.util.function.Function;

public final class ChainedQuestion<
    MessageType, SenderType extends ConversationPartner<MessageType>> {

  private final Question<MessageType, SenderType> firstQuestion;
  private Queue<
          Function<ComputeContext<MessageType, SenderType>, Question<MessageType, SenderType>>>
      questionsFunc = new ArrayDeque<>();

  public ChainedQuestion(Question<MessageType, SenderType> firstQuestion) {
    this.firstQuestion = Objects.requireNonNull(firstQuestion, "firstQuestion");
  }

  public Question<MessageType, SenderType> getFirstQuestion() {
    return this.firstQuestion;
  }

  public Question<MessageType, SenderType> getNextQuestion(
      ComputeContext<MessageType, SenderType> context) {
    Objects.requireNonNull(context, "context");
    if (this.questionsFunc.isEmpty()) {
      return null;
    }
    return this.questionsFunc.poll().apply(context);
  }

  public ChainedQuestion<MessageType, SenderType> addQuestion(
      Function<ComputeContext<MessageType, SenderType>, Question<MessageType, SenderType>> func) {
    Objects.requireNonNull(func, "func");
    this.questionsFunc.offer(func);
    return this;
  }

  public static final class ComputeContext<
      MessageType, SenderType extends ConversationPartner<MessageType>> {

    private final Question<MessageType, SenderType> previousQuestion;
    private final MessageType message;
    private final SenderType sender;

    public ComputeContext(
        Question<MessageType, SenderType> previousQuestion,
        MessageType message,
        SenderType sender) {
      this.previousQuestion = Objects.requireNonNull(previousQuestion, "previousQuestion");
      this.message = Objects.requireNonNull(message, "message");
      this.sender = Objects.requireNonNull(sender, "sender");
    }

    public Question<MessageType, SenderType> getPreviousQuestion() {
      return this.previousQuestion;
    }

    public MessageType getMessage() {
      return this.message;
    }

    public SenderType getSender() {
      return this.sender;
    }
  }
}

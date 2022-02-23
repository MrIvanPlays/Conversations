package com.mrivanplays.conversations.base.timeout;

import com.mrivanplays.conversations.base.Conversation;
import com.mrivanplays.conversations.base.ConversationContext.EndState;
import com.mrivanplays.conversations.base.ConversationManager;
import com.mrivanplays.conversations.base.DummyPartner;
import com.mrivanplays.conversations.base.question.Question;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TimeoutTest {

  private static final ConversationManager<String, DummyPartner> manager =
      new ConversationManager<>();

  @Test
  void testTimeout() {
    DummyPartner partner = new DummyPartner();
    ExecutorServiceTimeoutScheduler timeoutScheduler = new ExecutorServiceTimeoutScheduler();
    List<EndState> endedByTimeout = new ArrayList<>();
    Conversation<String, DummyPartner> convo =
        Conversation.<String, DummyPartner>newBuilder()
            .parentManager(manager)
            .withConversationPartner(partner)
            .withTimeoutScheduler(timeoutScheduler)
            .withQuestion(
                Question.<String, DummyPartner>newBuilder()
                    .withIdentifier("first")
                    .withMessage("Foo")
                    .withTimeout(1, TimeUnit.SECONDS)
                    .whenTimeout((consumed) -> consumed.sendMessage("foo"))
                    .build())
            .whenDone(context -> endedByTimeout.add(context.getEndState()))
            .build();

    convo.start();
    try {
      Thread.sleep(1100);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    Assertions.assertEquals(EndState.TIMEOUT, endedByTimeout.get(0));
    Assertions.assertEquals(2, partner.capturedMessages().size());
    Assertions.assertEquals("foo", partner.capturedMessages().get(1));
  }
}

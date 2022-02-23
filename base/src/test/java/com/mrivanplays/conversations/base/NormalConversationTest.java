package com.mrivanplays.conversations.base;

import com.mrivanplays.conversations.base.question.Question;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class NormalConversationTest {

  private static final ConversationManager<String, DummyPartner> manager =
      new ConversationManager<>();

  @Test
  void testNormalConversation() {
    DummyPartner partner = new DummyPartner();
    List<String> inputs = new ArrayList<>();
    Conversation<String, DummyPartner> convo =
        Conversation.<String, DummyPartner>newBuilder()
            .parentManager(manager)
            .withConversationPartner(partner)
            .withQuestion(Question.of("first", "Foo"))
            .withQuestion(Question.of("second", "Bar"))
            .whenDone(context -> {
              inputs.add(context.getInput("first"));
              inputs.add(context.getInput("second"));
            })
            .build();

    convo.start();
    convo.acceptInput("Bar");
    try {
      Thread.sleep(50);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
    convo.acceptInput("Foo");

    Assertions.assertEquals(2, partner.capturedMessages().size());
    Assertions.assertEquals("Foo", partner.capturedMessages().get(0));
    Assertions.assertEquals("Bar", partner.capturedMessages().get(1));
    Assertions.assertEquals(2, inputs.size());
    Assertions.assertEquals("Bar", inputs.get(0));
    Assertions.assertEquals("Foo", inputs.get(1));
  }
}

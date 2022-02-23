package com.mrivanplays.conversations.base.question;

import com.mrivanplays.conversations.base.Conversation;
import com.mrivanplays.conversations.base.ConversationManager;
import com.mrivanplays.conversations.base.DummyPartner;
import com.mrivanplays.conversations.base.question.InputValidator.ValidationResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class InputValidatorTest {

  private static final ConversationManager<String, DummyPartner> manager =
      new ConversationManager<>();

  @Test
  void testInputValidator() {
    DummyPartner partner = new DummyPartner();
    Conversation<String, DummyPartner> convo =
        Conversation.<String, DummyPartner>newBuilder()
            .parentManager(manager)
            .withConversationPartner(partner)
            .withQuestion(
                Question.<String, DummyPartner>newBuilder()
                    .withIdentifier("foo")
                    .withMessage("baz")
                    .withInputValidator(
                        foo -> {
                          if (foo.equalsIgnoreCase("bar")) {
                            return ValidationResult.fail("mama");
                          }
                          return ValidationResult.success();
                        })
                    .build())
            .whenDone(context -> context.getConversationPartner().sendMessage("baba"))
            .build();

    convo.start();
    convo.acceptInput("bar");

    Assertions.assertEquals(2, partner.capturedMessages().size());
    Assertions.assertEquals("baz", partner.capturedMessages().get(0));
    Assertions.assertEquals("mama", partner.capturedMessages().get(1));
  }
}

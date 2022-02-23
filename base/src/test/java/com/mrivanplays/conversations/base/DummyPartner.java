package com.mrivanplays.conversations.base;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class DummyPartner implements ConversationPartner<String> {

  private final UUID uuid = UUID.randomUUID();
  private final List<String> messages = new LinkedList<>();

  @Override
  public UUID getUniqueIdentifier() {
    return uuid;
  }

  @Override
  public void sendMessage(String message) {
    messages.add(message);
  }

  public List<String> capturedMessages() {
    return messages;
  }
}

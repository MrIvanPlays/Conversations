![license](https://img.shields.io/github/license/MrIvanPlays/Conversations.svg?style=for-the-badge)
![issues](https://img.shields.io/github/issues/MrIvanPlays/Conversations.svg?style=for-the-badge)
![api version](https://img.shields.io/maven-metadata/v?color=%20blue&label=latest%20version&metadataUrl=https%3A%2F%2Frepo.mrivanplays.com%2Frepository%2Fivan%2Fcom%2Fmrivanplays%2Fconversations-base%2Fmaven-metadata.xml&style=for-the-badge)
[![support](https://img.shields.io/discord/493674712334073878.svg?colorB=Blue&logo=discord&label=Support&style=for-the-badge)](https://mrivanplays.com/discord)
# Conversations

API for Conversations, built with cross-platform support in mind.<br>
**Javadocs:** https://jd.mrivanplays.com/conversations/

![gif](https://img.mrivanplays.com/hpxlgxebkh.gif)

# Why?

All came when I wanted to use the Bukkit Conversations API. It works fine, but the problem comes
when you have lots of questions. I don't like the idea of implementing `Prompt` for every single
question. I wanted something more like how Mojang went with brigadier. And this was born.

# OOB Supported platforms

| Platform                        | Artifact ID            | Message type                               | Sender Type                                  |
|---------------------------------|------------------------|--------------------------------------------|----------------------------------------------|
| Bukkit                          | `conversations-spigot` | `String`                                   | `Player` wrapped `BukkitConversationPartner` |
| Spigot                          | `conversations-spigot` | `net.md_5.bungee.api.chat.BaseComponent[]` | `Player` wrapped `SpigotConversationPartner` |
| Paper                           | `conversations-paper`  | `net.kyori.adventure.text.Component`       | `Player` wrapped `PaperConversationPartner`  |
| Whichever you want to implement | `conversations-base`   | Whatever you want                          | Whatever you want                            |

## How To (Maven)

```xml

<build>
  <plugins>
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-shade-plugin</artifactId>
      <version>3.1.1</version>
      <configuration>
        <relocations>
          <!-- Relocating is only necessary if you're shading for other library addition -->
          <relocation>
            <pattern>com.mrivanplays.conversations</pattern>
            <shadedPattern>[YOUR PLUGIN PACKAGE].conversations</shadedPattern> <!-- Replace this -->
          </relocation>
        </relocations>
      </configuration>
      <executions>
        <execution>
          <phase>package</phase>
          <goals>
            <goal>shade</goal>
          </goals>
        </execution>
      </executions>
    </plugin>
  </plugins>
</build>

<repositories>
  <repository>
    <id>ivan</id>
    <url>https://repo.mrivanplays.com/repository/ivan/</url>
  </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>com.mrivanplays</groupId>
    <!-- Types are listed above -->
    <artifactId>conversations-(type)</artifactId> <!-- Replace type -->
    <version>VERSION</version> <!-- Replace with latest version -->
    <scope>compile</scope>
  </dependency>
</dependencies>
```

## Basic example

The example is utilising Paper. There are bad practices in terms of the Bukkit API usage in this
example you shall NEVER do. This is a simple example which does not show the full potential of the
library.

```java
import com.mrivanplays.conversations.base.question.Question;
import com.mrivanplays.conversations.paper.PaperConversationManager;
import com.mrivanplays.conversations.paper.PaperConversationPartner;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class MainClass extends JavaPlugin {

  private PaperConversationManager convoManager;

  @Override
  public void onEnable() {
    getCommand("startconvo").setExecutor(this);
    this.convoManager = new PaperConversationManager(this);
  }

  @Override
  public boolean onCommand(
      @NotNull CommandSender sender,
      @NotNull Command command,
      @NotNull String label,
      String[] args) {
    Player player = (Player) sender;
    convoManager
        .newConversationBuilder(player)
        .withQuestion(
            Question.of("first", Component.text("What's your name?").color(NamedTextColor.GREEN)))
        .withQuestion(
            Question.of("second", Component.text("How old are you?").color(NamedTextColor.GREEN)))
        .whenDone(
            context -> {
              PaperConversationPartner conversationPartner = context.getConversationPartner();
              conversationPartner.sendMessage(
                  Component.text("Your name is: ")
                      .color(NamedTextColor.GREEN)
                      .append(context.getInput("first").color(NamedTextColor.YELLOW)));
              conversationPartner.sendMessage(
                  Component.text("You are ")
                      .color(NamedTextColor.GREEN)
                      .append(
                          context
                              .getInput("second")
                              .color(NamedTextColor.YELLOW)
                              .append(Component.text(" years old").color(NamedTextColor.GREEN))));
              conversationPartner.sendMessage(
                  Component.text("Thanks for taking our survey!").color(NamedTextColor.GOLD));
            })
        .build()
        .start();
    return true;
  }
}
```
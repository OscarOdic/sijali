package com.sijali.commands

import com.sijali.util.BotMessage
import org.scalatest.{AsyncFlatSpec, Matchers}
import com.sijali.util.TestMessage
import com.sijali.util.TestMessage._

class EmojiSpec extends AsyncFlatSpec with Matchers with TestMessage {
  "Emoji command" should "generate a message with other username and emoji icon" in {
    val command = s"sijali emoji testbot :smile: $channelName example message"

    val botMessageOpt = for {
      channelId <- channelIdOpt
    } yield BotMessage(
      channelId = channelId,
      message = "example message",
      username = Some("testbot"),
      iconEmoji = Some(":smile:")
    )

    assertFirstExecution(getChannelMessage(command), botMessageOpt)
  }
}

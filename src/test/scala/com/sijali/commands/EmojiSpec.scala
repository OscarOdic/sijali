package com.sijali.commands

import com.sijali.util.BotMessage
import org.scalatest.{AsyncFlatSpec, Matchers}
import com.sijali.util.TestMessage._

class EmojiSpec extends AsyncFlatSpec with Matchers {
  "Emoji command" should "generate a message with other username and emoji icon" in {
    val command = s"testbot :smile: $channelName example message".split(" ")

    val botMessage = BotMessage(
      channelId = channelId,
      message = "example message",
      username = Some("testbot"),
      iconEmoji = Some(":smile:")
    )

    Emoji.execute(command, channelName).map(exec =>
      exec should be(Right(botMessage))
    )
  }
}

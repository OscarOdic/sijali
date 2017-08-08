package com.sijali.commands

import com.sijali.util.{BotMessage, TestMessage}
import org.scalatest.{AsyncFlatSpec, Matchers}
import com.sijali.util.TestMessage._

class TestSpec extends AsyncFlatSpec with Matchers with TestMessage {
  "Test command" should "generate a test message" in {
    val command = "sijali test"

    val botMessage = BotMessage(
      channelId = imIdAdmin,
      message = "success"
    )

    assertMessage(getChannelMessage(command), Some(botMessage))
  }
}

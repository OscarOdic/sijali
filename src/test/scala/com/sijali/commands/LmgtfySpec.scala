package com.sijali.commands

import com.sijali.util.{BotMessage, TestMessage}
import org.scalatest.{AsyncFlatSpec, Matchers}
import com.sijali.util.TestMessage._

class LmgtfySpec extends AsyncFlatSpec with Matchers with TestMessage {
  "Lmgtfy command" should "generate a message with a link to lmgtfy" in {
    val command = s"sijali lmgtfy $channelName example message"

    val botMessageOpt = for {
      channelId <- channelIdOpt
    } yield BotMessage(
      channelId = channelId,
      message = "http://letmegooglethatforyou.com/?q=example+message"
    )

    assertMessage(getChannelMessage(command), botMessageOpt)
  }

  it should "generate an error if the channel, user or group is not found" in {
    val command = "sijali lmgtfy nothing example message"

    assertError(getChannelMessage(command), Some("Channel, User or Group nothing not found"))
  }
}

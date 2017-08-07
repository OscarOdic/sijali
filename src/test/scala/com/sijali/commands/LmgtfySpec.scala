package com.sijali.commands

import com.sijali.util.BotMessage
import org.scalatest.{AsyncFlatSpec, Matchers}
import com.sijali.util.TestMessage
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

    assertFirstExecution(getChannelMessage(command), botMessageOpt)
  }
}

package com.sijali.commands

import com.sijali.util.BotMessage
import org.scalatest.{AsyncFlatSpec, Matchers}
import com.sijali.util.TestMessage._

class LmgtfySpec extends AsyncFlatSpec with Matchers {
  "Lmgtfy command" should "generate a message with a link to lmgtfy" in {
    val command = s"$channelName example message".split(" ")

    val botMessage = BotMessage(
      channelId = channelId,
      message = "http://letmegooglethatforyou.com/?q=example+message"
    )

    Lmgtfy.execute(command, channelName).map(exec =>
      exec should be(Right(botMessage))
    )
  }
}

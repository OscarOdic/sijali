package com.sijali.commands

import com.sijali.util.BotMessage
import org.scalatest.{AsyncFlatSpec, Matchers}
import com.sijali.util.TestMessage._

class ChannelSpec extends AsyncFlatSpec with Matchers {
  "Channel command" should "generate a message for a channel slack" in {
    val command = s"$channelName example message".split(" ")

    val botMessage = BotMessage(
      channelId = channelId,
      message = "example message"
    )

    Channel.execute(command, channelName).map(exec =>
      exec should be(Right(botMessage))
    )
  }
}

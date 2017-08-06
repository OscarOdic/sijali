package com.sijali.commands

import com.sijali.util.BotMessage
import org.scalatest.{AsyncFlatSpec, Matchers}
import com.sijali.util.TestMessage._

class PrivateSpec extends AsyncFlatSpec with Matchers {
  "Private command" should "generate a private message" in {
    val command = s"$userName example message".split(" ")

    val botMessage = BotMessage(
      channelId = imId,
      message = "example message"
    )

    Private.execute(command, channelName).map(exec =>
      exec should be(Right(botMessage))
    )
  }
}

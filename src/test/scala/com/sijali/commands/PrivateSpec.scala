package com.sijali.commands

import com.sijali.util.BotMessage
import org.scalatest.{AsyncFlatSpec, Matchers}
import com.sijali.util.TestMessage
import com.sijali.util.TestMessage._

class PrivateSpec extends AsyncFlatSpec with Matchers with TestMessage {
  "Private command" should "generate a private message" in {
    val command = s"sijali private $userName example message"

    val botMessageOpt = for {
      imId <- imIdOpt
    } yield BotMessage(
      channelId = imId,
      message = "example message"
    )

    assertFirstExecution(getChannelMessage(command, imIdOpt), botMessageOpt)
  }
}

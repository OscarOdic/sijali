package com.sijali.commands

import com.sijali.util.{BotMessage, TestMessage}
import org.scalatest.{AsyncFlatSpec, Matchers}
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

    assertMessage(getChannelMessage(command), botMessageOpt)
  }

  it should "generate an error if the user is not found" in {
    val command = "sijali private nothing example message"

    assertError(getChannelMessage(command), Some("User nothing not found"))
  }
}

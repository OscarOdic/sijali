package com.sijali.commands

import com.sijali.util.{BotMessage, TestMessage}
import org.scalatest.{AsyncFlatSpec, Matchers}
import com.sijali.util.TestMessage._

class PollSpec extends AsyncFlatSpec with Matchers with TestMessage {
  "Poll command" should "generate a message with a poll" in {
    val command = s"""sijali poll $channelName "example message" :one: "one" :two: "two" :three: "three""""

    val botMessageOpt = for {
      channelId <- channelIdOpt
    } yield BotMessage(
      channelId = channelId,
      message = "*example message*\n:one: one\n:two: two\n:three: three",
      reactions = List("one", "two", "three")
    )

    assertMessage(getChannelMessage(command), botMessageOpt)
  }

  it should "generate an error if the channel, user or group is not found" in {
    val command = """sijali poll nothing "example message" :one: "one" :two: "two" :three: "three""""

    assertError(getChannelMessage(command), Some("Channel, User or Group nothing not found"))
  }
}

package com.sijali.commands

import com.sijali.util.BotMessage
import org.scalatest.{AsyncFlatSpec, Matchers}
import com.sijali.util.TestMessage._

class PollSpec extends AsyncFlatSpec with Matchers {
  "Poll command" should "generate a message with a poll" in {
    val command = s"""$channelName "example message" :one: "one" :two: "two" :three: "three"""".split(" ")

    val botMessage = BotMessage(
      channelId = channelId,
      message = "*example message*\n:one: one\n:two: two\n:three: three",
      reactions = List("one", "two", "three")
    )

    Poll.execute(command, channelName).map(exec =>
      exec should be(Right(botMessage))
    )
  }
}

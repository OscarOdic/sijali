package com.sijali.commands

import com.sijali.util.BotMessage
import org.scalatest.{AsyncFlatSpec, Matchers}
import com.sijali.util.TestMessage._

class TestSpec extends AsyncFlatSpec with Matchers {
  "Test command" should "generate a test message" in {
    val command = s"test".split(" ")

    val botMessage = BotMessage(
      channelId = imId,
      message = "success"
    )

    Test.execute(command, channelName).map(exec =>
      exec should be(Right(botMessage))
    )
  }
}

package com.sijali.commands

import com.sijali.util.BotMessage
import org.scalatest.{AsyncFlatSpec, Matchers}
import com.sijali.util.TestMessage
import com.sijali.util.TestMessage._

class TestSpec extends AsyncFlatSpec with Matchers with TestMessage {
  "Test command" should "generate a test message" in {
    val command = s"sijali test"

    val botMessageOpt = for {
      imId <- imIdOpt
    } yield BotMessage(
      channelId = imId,
      message = "success"
    )

    assertFirstExecution(getChannelMessage(command, imIdOpt), botMessageOpt)
  }
}

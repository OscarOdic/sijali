package com.sijali.commands

import com.sijali.util.BotMessage
import org.scalatest.{AsyncFlatSpec, Matchers}
import com.sijali.util.TestMessage
import com.sijali.util.TestMessage._

class ChannelSpec extends AsyncFlatSpec with Matchers with TestMessage {
  "Channel command" should "generate a message for a channel slack" in {
    val command = s"sijali channel $channelName example message"

    val botMessageOpt = for {
      channelId <- channelIdOpt
    } yield BotMessage(
      channelId = channelId,
      message = "example message"
    )

    assertFirstExecution(getChannelMessage(command), botMessageOpt)
  }
}

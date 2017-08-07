package com.sijali.commands

import com.sijali.util.{BotMessage, TestMessage}
import org.scalatest.{AsyncFlatSpec, Matchers}
import com.sijali.util.TestMessage._

class AsSpec extends AsyncFlatSpec with Matchers with TestMessage {
  "As command" should "generate a message in place of a user" in {
    val command = s"sijali as $userName $channelName example message"

    val botMessageOpt = for {
      channelId <- channelIdOpt
      user <- userOpt
    } yield BotMessage(
      channelId = channelId,
      message = "example message",
      username = Some(userName),
      iconUrl = user.profile.map(_.image_72)
    )

    assertFirstExecution(getChannelMessage(command), botMessageOpt)
  }
}

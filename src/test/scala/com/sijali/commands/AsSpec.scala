package com.sijali.commands

import com.sijali.util.BotMessage
import org.scalatest.{AsyncFlatSpec, Matchers}
import com.sijali.util.TestMessage._

class AsSpec extends AsyncFlatSpec with Matchers {
  "As command" should "generate a message in place of a user" in {
    val command = s"$userName $channelName example message".split(" ")

    val botMessage = BotMessage(
      channelId = channelId,
      message = "example message",
      username = Some(userName),
      iconUrl = user.profile.map(_.image_72)
    )

    As.execute(command, channelName).map(exec =>
      exec should be(Right(botMessage))
    )
  }
}

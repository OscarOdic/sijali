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

    assertMessage(getChannelMessage(command), botMessageOpt)
  }

  it should "generate an error if the user is not found" in {
    val command = s"sijali as nothing $channelName example message"

    assertError(getChannelMessage(command), Some("User nothing not found"))
  }

  it should "generate an error if the channel, user or group is not found" in {
    val command = s"sijali as $userName nothing example message"

    assertError(getChannelMessage(command), Some("Channel, User or Group nothing not found"))
  }
}

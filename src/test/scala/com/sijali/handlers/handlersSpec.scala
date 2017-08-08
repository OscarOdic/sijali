package com.sijali.handlers

import com.sijali.util.{BotMessage, TestMessage}
import org.scalatest.{AsyncFlatSpec, Matchers}
import com.sijali.util.TestMessage._
import com.typesafe.config.ConfigFactory

class handlersSpec extends AsyncFlatSpec with Matchers with TestMessage {

  "Admin" should "receive the private messages sent to the bot" in {
    val message = getChannelMessage("example message", imIdOpt)

    val botMessageOpt = for {
      user <- userOpt
    } yield BotMessage(
      channelId = imIdAdmin,
      message = "example message",
      username = Some(userName),
      iconUrl = user.profile.map(_.image_72)
    )

    assertMessage(message, botMessageOpt)
  }

  "Banned user" should "not be able to execute command" in {
    val command = s"sijali channel $channelName example message"
    val userId = userIdOpt.getOrElse(fail)

    val botMessageOpt = for {
      imId <- imIdOpt
    } yield BotMessage(
      channelId = imId,
      message = ConfigFactory.load().getString("ban.message")
    )

    assertMessage(getChannelMessage(command), botMessageOpt, Array(userId))
  }
}

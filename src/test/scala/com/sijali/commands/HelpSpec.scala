package com.sijali.commands

import com.sijali.util.{BotMessage, TestMessage}
import org.scalatest.{AsyncFlatSpec, Matchers}
import com.sijali.util.TestMessage._

class HelpSpec extends AsyncFlatSpec with Matchers with TestMessage {
  "Help command" should "show how to use the bot" in {
    val command = s"sijali help"

    val botMessageOpt = for {
      channelId <- channelIdOpt
    } yield BotMessage(
      channelId = channelId,
      message = "*test* - _Test if the bot is connected to slack, post a private to the admin user_\n\n" +
        "*channel* - _Post on a slack channel or group with the bot_\n\n" +
        "*private* - _Post on a slack private conversation with the bot_\n\n" +
        "*as* - _To chat on slack in place of a user_\n\n" +
        "*emoji* - _Post a slack message with other username and emoji icon_\n\n" +
        "*lmgtfy* - _Redirect a user to a google page, with lmgtfy website_\n\n" +
        "*poll* - _Post a poll on a slack channel, we can vote with all emojis_\n\n" +
        "*help* - _Show available commands and how to use it for the bot_\n"
    )

    assertFirstExecution(getChannelMessage(command), botMessageOpt)
  }

  it should "show how to use a command" in {
    val command = s"sijali help poll"

    val botMessageOpt = for {
      channelId <- channelIdOpt
    } yield BotMessage(
      channelId = channelId,
      message = "*NAME*\n\n  *poll* - Post a poll on a slack channel, we can vote with all emojis\n\n" +
        "*SYNOPSIS*\n\n  _\"sijali poll <channel/group> \"<question>\" :<firstEmoji>: \"<firstAnswer>\" [ :<secondEmoji>: \"<secondAnswer>\" ] ..._"
    )

    assertFirstExecution(getChannelMessage(command), botMessageOpt)
  }
}

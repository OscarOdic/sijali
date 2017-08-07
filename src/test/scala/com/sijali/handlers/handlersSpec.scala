package com.sijali.handlers

import com.sijali.util.BotMessage
import org.scalatest._
import com.sijali.util.TestMessage

class handlersSpec extends AsyncFlatSpec with Matchers with TestMessage {

  "A simple reaction" should "generate a simple response" in {
    val messageOpt = getChannelMessage("simple test message")
    val botMessageOpt = messageOpt.map(message => BotMessage(
      channelId = message.channel,
      message = "response simple test"
    ))

    assertFirstExecution(messageOpt, botMessageOpt)
  }

  "A reaction with user" should "generate a response for this user" in {
    val messageOpt = getChannelMessage("user test message")
    val botMessageOpt = messageOpt.map(message => BotMessage(
      channelId = message.channel,
      message = "response user test"
    ))

    assertFirstExecution(messageOpt, botMessageOpt)
  }

  it should "doesn't generate a response for another user" in {
    val messageOpt = getChannelMessage("user failed test message")

    messageOpt.map(message =>
      handlerMessage(message) should be(empty)
    ).getOrElse(fail)
  }

  "A reaction with not user" should "generate a response for another user" in {
    val messageOpt = getChannelMessage("user test 2 message")
    val botMessageOpt = messageOpt.map(message => BotMessage(
      channelId = message.channel,
      message = "response user test 2"
    ))

    assertFirstExecution(messageOpt, botMessageOpt)
  }

  it should "doesn't generate a response for this user" in {
    val messageOpt = getChannelMessage("user failed test 2 message")

    messageOpt.map(message =>
      handlerMessage(message) should be(empty)
    ).getOrElse(fail)
  }

  "A reaction with channel" should "generate a response for this channel" in {
    val messageOpt = getChannelMessage("channel test message")
    val botMessageOpt = messageOpt.map(message => BotMessage(
      channelId = message.channel,
      message = "response channel test"
    ))

    assertFirstExecution(messageOpt, botMessageOpt)
  }

  it should "doesn't generate a response for another channel" in {
    val messageOpt = getChannelMessage("channel failed test message")

    val response = messageOpt.flatMap(message =>
      handlerMessage(message).headOption
    ).getOrElse(fail)

    response.map(exec =>
      exec should be(Left(None))
    )
  }

  "A custom reaction" should "generate a custom response" in {
    val messageOpt = getChannelMessage("custom test message")
    val botMessageOpt = messageOpt.map(message => BotMessage(
      channelId = message.channel,
      message = "response custom test",
      username = Some("testBot"),
      iconEmoji = Some(":smile:")
    ))

    assertFirstExecution(messageOpt, botMessageOpt)
  }

  "No reaction" should "generate no response" in {
    val messageOpt = getChannelMessage("nothing")

    messageOpt.map(message =>
      handlerMessage(message) should be(empty)
    ).getOrElse(fail)
  }
}

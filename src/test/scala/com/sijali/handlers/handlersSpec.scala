package com.sijali.handlers

import com.sijali.util.BotMessage
import org.scalatest._
import com.sijali.util.TestMessage.getChannelMessage

class handlersSpec extends AsyncFlatSpec with Matchers {

  "A simple reaction" should "generate a simple response" in {
    val message = getChannelMessage("simple test message")
    val botMessage = BotMessage(
      channelId = message.channel,
      message = "response simple test"
    )

    handlerMessage(message).head.map(exec =>
      exec should be(Right(botMessage))
    )
  }

  "A reaction with user" should "generate a response for this user" in {
    val message = getChannelMessage("user test message")
    val botMessage = BotMessage(
      channelId = message.channel,
      message = "response user test"
    )

    handlerMessage(message).head.map(exec =>
      exec should be(Right(botMessage))
    )
  }

  it should "doesn't generate a response for another user" in {
    val message = getChannelMessage("user failed test message")

    handlerMessage(message) should be(empty)
  }

  "A reaction with not user" should "generate a response for another user" in {
    val message = getChannelMessage("user test 2 message")
    val botMessage = BotMessage(
      channelId = message.channel,
      message = "response user test 2"
    )

    handlerMessage(message).head.map(exec =>
      exec should be(Right(botMessage))
    )
  }

  it should "doesn't generate a response for this user" in {
    val message = getChannelMessage("user failed test 2 message")

    handlerMessage(message) should be(empty)
  }

  "A reaction with channel" should "generate a response for this channel" in {
    val message = getChannelMessage("channel test message")
    val botMessage = BotMessage(
      channelId = message.channel,
      message = "response channel test"
    )

    handlerMessage(message).head.map(exec =>
      exec should be(Right(botMessage))
    )
  }

  it should "doesn't generate a response for another channel" in {
    val message = getChannelMessage("channel failed test message")

    handlerMessage(message).head.map(exec =>
      exec should be(Left(None))
    )
  }

  "A custom reaction" should "generate a custom response" in {
    val message = getChannelMessage("custom test message")
    val botMessage = BotMessage(
      channelId = message.channel,
      message = "response custom test",
      username = Some("testBot"),
      iconEmoji = Some(":smile:")
    )

    handlerMessage(message).head.map(exec =>
      exec should be(Right(botMessage))
    )
  }
}

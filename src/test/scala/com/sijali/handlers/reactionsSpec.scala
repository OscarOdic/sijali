package com.sijali.handlers

import com.sijali.util.{BotMessage, TestMessage}
import org.scalatest._

class reactionsSpec extends AsyncFlatSpec with Matchers with TestMessage {

  "Simple reaction" should "generate a simple response" in {
    val messageOpt = getChannelMessage("simple test message")
    val botMessageOpt = messageOpt.map(message => BotMessage(
      channelId = message.channel,
      message = "response simple test"
    ))

    assertMessage(messageOpt, botMessageOpt)
  }

  "Reaction with user" should "generate a response for this user" in {
    val messageOpt = getChannelMessage("user test message")
    val botMessageOpt = messageOpt.map(message => BotMessage(
      channelId = message.channel,
      message = "response user test"
    ))

    assertMessage(messageOpt, botMessageOpt)
  }

  it should "doesn't generate a response for another user" in {
    val messageOpt = getChannelMessage("user failed test message")

    messageOpt.map(message =>
      handlerMessage(message) should be(empty)
    ).getOrElse(fail)
  }

  "Reaction with not user" should "generate a response for another user" in {
    val messageOpt = getChannelMessage("user test 2 message")
    val botMessageOpt = messageOpt.map(message => BotMessage(
      channelId = message.channel,
      message = "response user test 2"
    ))

    assertMessage(messageOpt, botMessageOpt)
  }

  it should "doesn't generate a response for this user" in {
    val messageOpt = getChannelMessage("user failed test 2 message")

    messageOpt.map(message =>
      handlerMessage(message) should be(empty)
    ).getOrElse(fail)
  }

  "Reaction with channel" should "generate a response for this channel" in {
    val messageOpt = getChannelMessage("channel test message")
    val botMessageOpt = messageOpt.map(message => BotMessage(
      channelId = message.channel,
      message = "response channel test"
    ))

    assertMessage(messageOpt, botMessageOpt)
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

  "Custom reaction" should "generate a custom response" in {
    val messageOpt = getChannelMessage("custom test message")
    val botMessageOpt = messageOpt.map(message => BotMessage(
      channelId = message.channel,
      message = "response custom test",
      username = Some("testBot"),
      iconEmoji = Some(":smile:")
    ))

    assertMessage(messageOpt, botMessageOpt)
  }

  "No reaction" should "generate no response" in {
    val messageOpt = getChannelMessage("nothing")

    messageOpt.map(message =>
      handlerMessage(message) should be(empty)
    ).getOrElse(fail)
  }

  "Multiple reaction" should "generate a random response" in {
    val messageSent = getChannelMessage("multiple test message").getOrElse(fail)

    val messageGenerated = handlerMessage(messageSent).headOption.getOrElse(fail)

    messageGenerated map {
      case Left(_) => fail
      case Right(botMessage) =>
        List("response multiple test 1", "response multiple test 2") should contain(botMessage.message)
    }
  }
}

package com.sijali.util

import com.sijali.handlers.handlerMessage
import com.typesafe.config.ConfigFactory
import org.scalatest.{Assertion, AsyncFlatSpec, Matchers}
import slack.models.{Message, User}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.DurationInt

object TestMessage {
  val userName: String = ConfigFactory.load().getString("test.user")
  val channelName: String = ConfigFactory.load().getString("test.channel")

  val channelIdOpt: Option[String] = Await.result(Slack.getChanIdByName(channelName), 5.seconds).right.toOption
  val userIdOpt: Option[String] = Await.result(Slack.getUserIdByName(userName), 5.seconds).right.toOption
  val imIdOpt: Option[String] = Await.result(Slack.getImIdByUserName(userName), 5.seconds).right.toOption

  val userOpt: Option[User] = userIdOpt.map(userId => Await.result(Slack.getUserById(userId), 5.seconds))
}

trait TestMessage extends AsyncFlatSpec with Matchers {
  def getChannelMessage(text: String, channel: Option[String] = TestMessage.channelIdOpt): Option[Message] = {
    for {
      channelId <- channel
      userId <- TestMessage.userIdOpt
    } yield Message(
      ts = "",
      channel = channelId,
      user = userId,
      text = text,
      None
    )
  }

  def assertFirstExecution(messageSent: Option[Message], messageExpected: Option[BotMessage]): Future[Assertion] = {
    val response = messageSent.flatMap(message =>
      handlerMessage(message).headOption
    ).getOrElse(fail)

    val botMessage = messageExpected.getOrElse(fail)

    response.flatMap(exec =>
      exec should be(Right(botMessage))
    )
  }
}

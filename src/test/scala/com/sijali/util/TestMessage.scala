package com.sijali.util

import com.typesafe.config.ConfigFactory
import slack.models.{Message, User}

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

object TestMessage {
  val userName: String = ConfigFactory.load().getString("test.user")
  val channelName: String = ConfigFactory.load().getString("test.channel")

  val channelId: String = Await.result(Slack.getChanIdByName(channelName), 5.seconds).right.get
  val userId: String = Await.result(Slack.getUserIdByName(userName), 5.seconds).right.get
  val imId: String = Await.result(Slack.getImIdByUserName(userName), 5.seconds).right.get

  val user: User = Await.result(Slack.getUserById(userId), 5.seconds)

  def getChannelMessage(text: String): Message = Message(
    ts = "",
    channel = channelId,
    user = userId,
    text = text,
    None
  )
}

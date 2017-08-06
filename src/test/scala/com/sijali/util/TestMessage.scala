package com.sijali.util

import com.typesafe.config.ConfigFactory
import slack.models.Message
import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

object TestMessage {
  val channelId: String = Await.result(Slack.getChanIdByName(ConfigFactory.load().getString("test.channel")), 5.seconds).right.get
  val userId: String = Await.result(Slack.getUserIdByName(ConfigFactory.load().getString("test.user")), 5.seconds).right.get

  def getChannelMessage(text: String): Message = Message(
    ts = "",
    channel = channelId,
    user = userId,
    text = text,
    None
  )
}

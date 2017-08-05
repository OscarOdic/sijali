package com.sijali.util

import com.sijali.SlackBot.apiClient
import com.sijali.SlackBot.system
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/** A bot message to send on slack
  *
  * @param channelId The id of channel where the message is going to be send
  * @param message The message to send
  * @param asUser If the bot is a user (with his defaults slack parameters
  * @param username The name of the bot
  * @param iconUrl The avatar of bot with url
  * @param iconEmoji The avatar of the bot with emoji
  * @param reactions Some reactions to add on the message
  */
case class BotMessage(
                  channelId: String,
                  message: String,
                  asUser: Option[Boolean] = None,
                  username: Option[String] = None,
                  iconUrl: Option[String] = None,
                  iconEmoji: Option[String] = None,
                  reactions: List[String] = List()
                  ) {

  /** Send the message on Slack
    *
    * @return The Future of the Timestamp
    */
  def send: Future[String] = {
    val future = username match {
      case None => apiClient.postChatMessage(
        channelId = channelId,
        text = message,
        asUser = Some(true)
      )
      case _ => apiClient.postChatMessage(
        channelId = channelId,
        text = message,
        asUser = asUser,
        username = username,
        iconUrl = iconUrl,
        iconEmoji = iconEmoji
      )
    }

    if (reactions.nonEmpty)
      future onSuccess {
        case ts => reactions.foreach(r => {
          apiClient.addReactionToMessage(
            emojiName = r,
            timestamp = ts,
            channelId = channelId
          )
        })
      }

    future
  }
}

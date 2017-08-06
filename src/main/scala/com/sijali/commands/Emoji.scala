package com.sijali.commands

import com.sijali.SlackBot
import com.sijali.commands.models.Command
import com.sijali.util.BotMessage
import com.sijali.util.Slack._

import scala.concurrent.Future

/** Emoji command : Post a slack message with other username and emoji icon */
object Emoji extends Command {

  /** Execute the command with the name "emoji" */
  val name = "emoji"

  /** Description of the command */
  val description = "Post a slack message with other username and emoji icon"

  /** The synopsis of the command */
  val synopsis = s"${SlackBot.botName} emoji <botname> :<emojiname>: <channel/user/group> <message>"

  /** -e */
  val short = Some("-e")

  /** Execute the command with some parameters
    *
    * @param params userName | :emojiName: (like :joy:) | target (channel/user/group) | message
    * @param channel The channel where is executed the command
    *
    * @return The future of a bot message, or an error
    */
  def execute(params: Array[String], channel: String): Future[Execution] =
    getChanIdByName(params(2)) map {
      case Left(e) => Left(Some(e))
      case Right(c) => Right(BotMessage(
        channelId = c,
        message = params.drop(3).mkString(" "),
        username = Some(params.head),
        iconEmoji = Some(params(1))
      ))
    }
}

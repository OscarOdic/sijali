package com.sijali.commands

import com.sijali.SlackBot
import com.sijali.commands.models.Command
import com.sijali.util.BotMessage
import com.sijali.util.Slack._

import scala.concurrent.Future

/** Channel command : Post on a slack channel or group with the bot */
object Channel extends Command{

  /** Execute the command with the name "channel" */
  val name = "channel"

  /** Description of the command */
  val description = "Post on a slack channel or group with the bot"

  /** The synopsis of the command */
  val synopsis = s"${SlackBot.botName} channel <channel> <message>"

  /** -c */
  val short = Some("-c")

  /** Execute the command with some parameters
    *
    * @param params channel/group name | message
    * @param channel The channel where is executed the command
    *
    * @return The future of a bot message, or an error
    */
  def execute(params: Array[String], channel: String): Future[Execution] =
    getChannelIdByName(params.head) map {
      case Left(e) => Left(Some(e))
      case Right(channelId) => Right(BotMessage(
        channelId = channelId,
        message = params.tail.mkString(" ")
      ))
    }
}

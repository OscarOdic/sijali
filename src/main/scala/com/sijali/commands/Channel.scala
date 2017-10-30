package com.sijali.commands

import com.sijali.SlackBot
import com.sijali.commands.models.Command
import com.sijali.util.BotMessage
import com.sijali.util.Slack._

import scala.concurrent.ExecutionContext.Implicits.global
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

  /** Generate a parser to execute with parameters
    *
    * @param channel The channel where is executed the command
    *
    * @return The parser
    */
  def parser(channel: String): Parser[Future[Execution]] =
    for {
      channelName <- """\w+""".r
      message <- """(?s).+$""".r.? ^^ (_.getOrElse(""))
    } yield getChannelIdByName(channelName) map {
      case Left(e) => Left(Some(e))
      case Right(channelId) => Right(BotMessage(
        channelId = channelId,
        message = message
      ))
    }
}

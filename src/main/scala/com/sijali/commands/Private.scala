package com.sijali.commands

import com.sijali.SlackBot
import com.sijali.commands.models.Command
import com.sijali.util._
import scala.concurrent.Future

/** Private command : Post on a slack private conversation with the bot */
object Private extends Command{

  /** Execute the command with the name "private" */
  val name = "private"

  /** Description of the command */
  val description = "Post on a slack private conversation with the bot"

  /** The synopsis of the command */
  val synopsis = s"${SlackBot.botName} private <username> <channel>"

  /** -p */
  val short = Some("-p")

  /** Execute the command with some parameters
    *
    * @param params user | message
    * @param channel The channel where is executed the command
    *
    * @return The future of a bot message, or an error
    */
  def execute(params: Array[String], channel: String): Future[Execution] =
    getImIdByUserName(params.head) map {
      case Left(e) => Left(Some(e))
      case Right(c) => Right(BotMessage(
        channelId = c,
        message = params.tail.mkString(" ")
      ))
    }
}

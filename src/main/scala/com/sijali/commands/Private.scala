package com.sijali.commands

import com.sijali.SlackBot
import com.sijali.commands.models.Command
import com.sijali.util.BotMessage
import com.sijali.util.Slack._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/** Private command : Post on a slack private conversation with the bot */
object Private extends Command{

  /** Execute the command with the name "private" */
  val name = "private"

  /** Description of the command */
  val description = "Post on a slack private conversation with the bot"

  /** The synopsis of the command */
  val synopsis = s"${SlackBot.botName} private <username> <message>"

  /** -p */
  val short = Some("-p")

  /** Generate a parser to execute with parameters
    *
    * @param channel The channel where is executed the command
    *
    * @return The parser
    */
  def parser(channel: String): Parser[Future[Execution]] =
    for {
      user <- """\S+""".r
      message <- """(?s).+$""".r.? ^^ (_.getOrElse(""))
    } yield user match {
      case "all" => Future(Right(BotMessage(
        channelId = user,
        message = message
      )))
      case username => getImIdByUserName(username) map {
        case Left(e) => Left(Some(e))
        case Right(c) => Right(BotMessage(
          channelId = c,
          message = message
        ))
      }
    }
}

package com.sijali.commands

import com.sijali.SlackBot
import com.sijali.commands.models.Command
import com.sijali.util.BotMessage
import com.sijali.util.Slack._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/** As command : To chat on slack in place of a user */
object As extends Command {

  /** Execute the command with the name "as" */
  val name = "as"

  /** Description of the command */
  val description = "To chat on slack in place of a user"

  /** The synopsis of the command */
  val synopsis = s"${SlackBot.botName} as <username> <channel/user/group> <message>"

  /** No short name */
  val short = None

  /** Generate a parser to execute with parameters
    *
    * @param channel The channel where is executed the command
    *
    * @return The parser
    */
  def parser(channel: String): Parser[Future[Execution]] =
    for {
      userName <- """\w+""".r
      channelName <- """\w+""".r
      message <- """.+$""".r.? ^^ (_.getOrElse(""))
    } yield getUserByName(userName) flatMap {
      case Left(e) => Future(Left(Some(e)))
      case Right(u) => getChanIdByName(channelName) map {
        case Left(e) => Left(Some(e))
        case Right(channelId) => Right(BotMessage(
          channelId = channelId,
          message = message,
          username = Some(u.name),
          iconUrl = u.profile.map(_.image_72)
        ))
      }
    }
}

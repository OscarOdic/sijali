package com.sijali.commands

import com.sijali.SlackBot
import com.sijali.commands.models.Command
import com.sijali.util._
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

  /** Execute the command with some parameters
    *
    * @param params userName (declared in application.conf) | target (channel/user/group) | message
    * @param channel The channel where is executed the command
    *
    * @return The future of a bot message, or an error
    */
  def execute(params: Array[String], channel: String): Future[Execution] =
    getUserByName(params.head).flatMap{
      case Left(e) => Future(Left(Some(e)))
      case Right(u) => getChanIdByName(params(1)).map {
        case Left(e) => Left(Some(e))
        case Right(channelId) => Right(BotMessage(
          channelId = channelId,
          message = params.drop(2).mkString(" "),
          username = Some(u.name),
          iconUrl = u.profile.map(_.image_72)
        ))
      }
    }
}

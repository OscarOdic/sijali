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
      user <- "<@" ~> """\w+""".r <~ ">"
      channel <- ("<@" ~> """\w+""".r <~ ">") | ("<#" ~> """\w+""".r <~ """\|\w+>""".r)
      message <- """(?s).+$""".r.? ^^ (_.getOrElse(""))
    } yield getUserById(user).map(u =>
      Right(BotMessage(
        channelId = channel,
        message = message,
        username = u.profile.get.real_name,
        iconUrl = u.profile.map(_.image_72)
      ))
    )
}

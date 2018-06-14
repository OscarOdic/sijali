package com.sijali.commands

import com.sijali.SlackBot
import com.sijali.commands.models.Command
import com.sijali.util.BotMessage
import com.sijali.util.Slack._
import java.net.URLEncoder

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/** Lmgtfy command : Redirect a user to a google page, with lmgtfy website **/
object Lmgtfy extends Command {

  /** Execute the command with the name "lmgtfy" */
  val name = "lmgtfy"

  /** Description of the command */
  val description = "Redirect a user to a google page, with lmgtfy website"

  /** The synopsis of the command */
  val synopsis = s"${SlackBot.botName} lmgtfy <channel/user/group> <message>"

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
      channel <- ("<@" ~> """\w+""".r <~ ">") | ("<#" ~> """\w+""".r <~ """\|\w+>""".r)
      message <- """(?s).+$""".r.? ^^ (_.getOrElse(""))
    } yield Future(Right(BotMessage(
      channelId = channel,
      message = "http://letmegooglethatforyou.com/?q=" + URLEncoder.encode(message, "UTF-8")
    )))
}

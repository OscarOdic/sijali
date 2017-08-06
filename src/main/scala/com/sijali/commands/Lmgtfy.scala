package com.sijali.commands

import com.sijali.SlackBot
import com.sijali.commands.models.Command
import com.sijali.util.BotMessage
import com.sijali.util.Slack._
import java.net.URLEncoder

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

  /** Execute the command with some parameters
    *
    * @param params target (channel/user/group) | message
    * @param channel The channel where is executed the command
    *
    * @return The future of a bot message, or an error
    */
  def execute(params: Array[String], channel: String): Future[Execution] =
    getChanIdByName(params.head) map {
      case Left(e) => Left(Some(e))
      case Right(c) => Right(BotMessage(
        channelId = c,
        message = "http://letmegooglethatforyou.com/?q=" + URLEncoder.encode(params.tail.mkString(" "), "UTF-8")
      ))
    }
}

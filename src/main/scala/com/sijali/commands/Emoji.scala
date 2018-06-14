package com.sijali.commands

import com.sijali.SlackBot
import com.sijali.commands.models.Command
import com.sijali.util.BotMessage
import com.sijali.util.Slack._

import scala.concurrent.ExecutionContext.Implicits.global
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

  /** Generate a parser to execute with parameters
    *
    * @param channel The channel where is executed the command
    *
    * @return The parser
    */
  def parser(channel: String): Parser[Future[Execution]] =
    for {
      username <- """\S+""".r
      emoji <- """:\w+:""".r
      channel <- ("<@" ~> """\w+""".r <~ ">") | ("<#" ~> """\w+""".r <~ """\|\w+>""".r)
      message <- """(?s).+$""".r.? ^^ (_.getOrElse(""))
    } yield Future(Right(BotMessage(
      channelId = channel,
      message = message,
      username = Some(username),
      iconEmoji = Some(emoji)
    )))
}

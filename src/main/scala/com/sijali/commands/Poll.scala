package com.sijali.commands

import com.sijali.SlackBot
import com.sijali.commands.models.Command
import com.sijali.util.BotMessage
import com.sijali.util.Slack._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/** Poll command : Post a poll on a slack channel, we can vote with all emojis */
object Poll extends Command {

  /** Execute the command with the name "poll" */
  val name = "poll"

  /** Description of the command */
  val description = "Post a poll on a slack channel, we can vote with all emojis"

  /** The synopsis of the command */
  val synopsis = s""""${SlackBot.botName} poll <channel/group> "<question>" :<firstEmoji>: "<firstAnswer>" [ :<secondEmoji>: "<secondAnswer>" ] ..."""

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
      channel <- "\"" ~> ("<@" ~> """\w+""".r <~ ">") | ("<#" ~> """\w+""".r <~ """\|\w+>""".r)
      sentence <- "\"" ~> """[^"]+""".r <~ "\""
      choices <- rep(for {
        emoji <- ":" ~> """[^:]+""".r <~ ":"
        vote <- "\"" ~> """[^"]+""".r <~ "\""
      } yield (emoji, vote))
    } yield {
      val message = s"*$sentence*\n" + choices.map(choice => s":${choice._1}: ${choice._2}").mkString("\n")
      Future(Right(BotMessage(
        channelId = channel,
        message = message,
        reactions = choices.map(_._1)
      )))
    }
}

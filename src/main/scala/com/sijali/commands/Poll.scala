package com.sijali.commands

import com.sijali.SlackBot
import com.sijali.commands.models.Command
import com.sijali.util.BotMessage
import com.sijali.util.Slack._
import scala.concurrent.Future
import scalaz.syntax.traverse._
import scalaz.std.list._
import scalaz.std.option._

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

  /** Execute the command with some parameters
    *
    * @param params channel | "question" | :firstEmoji: | "firstAnswer" | ...
    * @param channel The channel where is executed the command
    *
    * @return The future of a bot message, or an error
    */
  def execute(params: Array[String], channel: String): Future[Execution] = {
    val regex = """("[^"]+")\s+(:.+$)""".r
    val regex(sentence, choiceString) = params.tail.mkString(" ")

    val choicesOpt = """:\w+:\s+"\w+"""".r.findAllIn(choiceString).toList.map(c => for {
      emoji <- """:.+:""".r.findFirstIn(c)
      vote <- """".+"""".r.findFirstIn(c).map(_.replace("\"", ""))
    } yield (emoji, vote)).sequence

    choicesOpt match {
      case None => Future(Left(Some("Unable to read choices")))
      case Some(choices) =>
        val message = sentence.replace("\"", "*") + "\n" +
          choices.map(choice => choice._1 + " " + choice._2).mkString("\n")
        getChanIdByName(params.head) map {
          case Left(e) => Left(Some(e))
          case Right(c) => Right(BotMessage(
            channelId = c,
            message = message,
            reactions = choices.map(_._1.replace(":",""))
          ))
        }
    }
  }
}

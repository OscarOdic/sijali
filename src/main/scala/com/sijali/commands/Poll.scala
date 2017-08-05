package com.sijali.commands

import com.sijali.SlackBot
import com.sijali.commands.models.Command
import com.sijali.util._
import scala.concurrent.Future

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

    val choices = """:[^:]+:\s+"[^"]+"""".r.findAllIn(choiceString).toList .map { c =>
      (""":.+:""".r.findFirstIn(c).get, """".+"""".r.findFirstIn(c).get.replace("\"", ""))
    }

    val message = sentence.replace("\"", "*") + "\n" + choices.map(choice => choice._1 + " " + choice._2).mkString("\n")

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

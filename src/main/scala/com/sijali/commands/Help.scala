package com.sijali.commands

import com.sijali.SlackBot
import com.sijali.commands.models.Command
import com.sijali.util.BotMessage
import com.sijali.util.Slack._

import scala.concurrent.Future

/** Help command : Show available commands and how to use it for the bot */
object Help extends Command {

  /** Execute the command with the name "help" */
  val name = "help"

  /** Description of the command */
  val description = "Show available commands and how to use it for the bot"

  /** The synopsis of the command */
  val synopsis = s"${SlackBot.botName} help [ <command> ]"

  /** -h */
  val short = Some("-h")

  /** Get the list of the commands with their descriptions
    *
    * @return The list to display on slack
    */
  private def commandsName: String =
    commands.map(c => s"*${c.name}* - _${c.description}_\n").mkString("\n")

  /** Get The help for a specific command
    *
    * @param command The command to get help
    * @return The help to display
    */
  private def commandInfo(command: Command): String =
    s"""*NAME*
       |
       |  *${command.name}* - ${command.description}
       |
       |*SYNOPSIS*
       |
       |  _${command.synopsis}_""".stripMargin

  /** Execute the command with some parameters
    *
    * @param params empty/command
    * @param channel The channel where is executed the command
    *
    * @return The future of a bot message, or an error
    */
  def execute(params: Array[String], channel: String): Future[Execution] =
    Future(Right(BotMessage(
      channelId = channel,
      message = params.toList match {
        case Nil => commandsName
        case command::_ => commands.find(_.name == command).map(commandInfo).getOrElse(commandsName)
      }
    )))
}

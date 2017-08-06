package com.sijali

import com.sijali.commands.models.Command
import com.sijali.util.BotMessage
import scala.concurrent.Future

/** Commands executions */
package object commands {
  type Execution = Either[Option[String], BotMessage]

  /** Array of commands recognized by the bot */
  val commands: Array[Command] = Array(
    Test,
    Channel,
    Private,
    As,
    Emoji,
    Lmgtfy,
    Poll,
    Help
  )

  /** Execute the right command with its name and its parameters
    *
    * @param commandName The name (or short name) of the command to execute
    * @param params The parameters of the command, split words by spaces after the command name
    */
  def execute(commandName: String, params: Array[String], channel: String): Option[Future[Execution]] =
    commands
      .find(c => c.name == commandName || c.short.getOrElse(false) == commandName)
      .map(_.execute(params, channel))
}

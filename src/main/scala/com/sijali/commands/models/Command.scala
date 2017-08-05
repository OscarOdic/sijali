package com.sijali.commands.models

import com.sijali.commands.Execution
import scala.concurrent.Future

/** A command for our Slack bot */
trait Command {

  /** The name of the command, to call it */
  val name: String

  /** Description of the command */
  val description: String

  /** The synopsis of the command */
  val synopsis: String

  /** A short name, started with a "-", to call it too instead of the name */
  val short: Option[String]

  /** Execute the command with some parameters
    *
    * @param params The parameters of the command, split words by spaces after the command name
    * @param channel The channel where is executed the command
    *
    * @return The future of a bot message, or an error
    */
  def execute(params: Array[String], channel: String): Future[Execution]
}

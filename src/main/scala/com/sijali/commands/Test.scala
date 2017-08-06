package com.sijali.commands

import com.sijali.SlackBot
import com.sijali.commands.models.Command
import com.sijali.util._
import com.typesafe.config.ConfigFactory
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

/** Test command : Test if the bot is connected to slack, post a private to the admin user */
object Test extends Command {

  /** Execute the command with the name "test" */
  val name = "test"

  /** Description of the command */
  val description = "Test if the bot is connected to slack, post a private to the admin user"

  /** The synopsis of the command */
  val synopsis = s"${SlackBot.botName} test"

  /** -t */
  val short = Some("-t")

  /** Execute the command with some parameters
    *
    * @param params empty
    * @param channel The channel where is executed the command
    *
    * @return The future of a bot message, or an error
    */
  def execute(params: Array[String], channel: String): Future[Execution] =
    getImIdByUserId(ConfigFactory.load().getString("admin.id")).map(c =>
      Right(BotMessage(
        channelId = c,
        message = "success"
      ))
    )
}

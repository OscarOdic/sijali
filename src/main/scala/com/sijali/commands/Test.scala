package com.sijali.commands

import com.sijali.SlackBot
import com.sijali.commands.models.Command
import com.sijali.util.BotMessage
import com.sijali.util.Slack._
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

  /** Generate a parser to execute with parameters
    *
    * @param channel The channel where is executed the command
    *
    * @return The parser
    */
  def parser(channel: String): Parser[Future[Execution]] =
    Parser(input => Success(
      getImIdByUserId(ConfigFactory.load().getString("admin.id")).map(c =>
        Right(BotMessage(
          channelId = c,
          message = "success"
        ))
      ), input
    ))
}

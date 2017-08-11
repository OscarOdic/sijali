package com.sijali.commands.models

import com.sijali.commands.Execution

import scala.concurrent.Future
import scala.util.parsing.combinator.RegexParsers

/** A command for our Slack bot */
trait Command extends RegexParsers {

  /** The name of the command, to call it */
  val name: String

  /** Description of the command */
  val description: String

  /** The synopsis of the command */
  val synopsis: String

  /** A short name, started with a "-", to call it too instead of the name */
  val short: Option[String]

  /** Generate a parser to execute with parameters
    *
    * @param channel The channel where is executed the command
    *
    * @return The parser
    */
  protected def parser(channel: String): Parser[Future[Execution]]

  /** Execute the command with some parameters
    *
    * @param params The parameters of the command, split words by spaces after the command name
    * @param channel The channel where is executed the command
    *
    * @return The future of a bot message, or an error
    */
  def execute(params: String, channel: String): Option[Future[Execution]] =
    parse(parser(channel), params).map(Some(_)).getOrElse(None)
}

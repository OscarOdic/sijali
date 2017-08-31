package com.sijali

import akka.actor.{ActorRef, ActorSystem}
import com.sijali.util.BotMessage
import com.sijali.util.Slack._
import com.typesafe.config.ConfigFactory
import handlers.handlerMessage
import java.util.NoSuchElementException

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}
import slack.api.SlackApiClient
import slack.models.Message
import slack.rtm.SlackRtmClient

object SlackBot {
  val token: String = ConfigFactory.load().getString("bot.token")
  val botName: String = ConfigFactory.load().getString("bot.name")
  implicit val system: ActorSystem = ActorSystem(botName)
  val apiClient = SlackApiClient(token)
  val rtmClient = SlackRtmClient(token)

  /** Get the bot id
    *
    * @return bot id
    */
  def botId: String = rtmClient.state.self.id

  /** Print and send to an error message on the channel
    * and if the command is used in a private message, send also the error to the admin user
    *
    * @param err The error
    * @param channel The channel to send the error
    */
  private def sendErrorMessage(err: Throwable, channel: Option[String] = None): Unit = {
    val error = "[" + Console.RED + "error" + Console.RESET + "] "
    Console.err.println(error + err.getMessage) // scalastyle:ignore
    val message = "*[error]* _" + err.getMessage + "_"

    def sendToAdmin(channel: Option[String] = None): Unit =
      getImIdByUserId(ConfigFactory.load.getString("admin.id")) onSuccess {
        case adminChannel => if (!channel.contains(adminChannel)) BotMessage(adminChannel, message).send
      }

    channel match {
      case Some(c) =>
        if (c.startsWith("D")) sendToAdmin(Some(c))
        BotMessage(c, message).send
      case None =>
        sendToAdmin()
    }
  }

  /** Print a info message
    *
    * @param m The message
    */
  private def printInfoMessage(m: Message): Unit = {
    val info = "[" + Console.BLUE + "info" + Console.RESET + "] "
    println(info + "Received new message") // scalastyle:ignore
    println(info + m.toString) // scalastyle:ignore
  }

  /** Listen all slack event received by bot and execute handlers
    *
    * @return The ActorRef of the bot
    */
  private def listenEvent(): Try[ActorRef] = Try {
    rtmClient.onMessage { m =>
      printInfoMessage(m)
      Future.sequence(handlerMessage(m)) onComplete {
        case Success(l) => l foreach {
          case Left(Some(e)) => sendErrorMessage(new Exception(e), Some(m.channel))
          case Right(message) => message.send
          case Left(None) => ;
          case _ => sendErrorMessage(new Exception("Unknown error"), Some(m.channel))
        }
        case Failure(e) => e match {
          case _: NoSuchElementException => ;
          case _ => sendErrorMessage(e, Some(m.channel))
        }
      }
    }
  }

  def main(args: Array[String]): Unit =
    listenEvent().failed.map(sendErrorMessage(_))
}

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
  implicit val system = ActorSystem(botName)
  val apiClient = SlackApiClient(token)
  val rtmClient = SlackRtmClient(token)

  /** Get the bot id
    *
    * @return bot id
    */
  def botId: String = rtmClient.state.self.id

  /** Print and send to admin slack user a error message
    *
    * @param err The error
    */
  private def sendErrorMessage(err: Throwable): Unit = {
    val error = "[" + Console.RED + "error" + Console.RESET + "] "
    Console.err.println(error + err.getMessage) // scalastyle:ignore
    err.getStackTrace.foreach (s =>
      Console.err.println(error + "at " +  s.toString) // scalastyle:ignore
    )
    getImIdByUserId(ConfigFactory.load.getString("admin.id")) onSuccess {
      case channel => BotMessage(channel, "*[error]* _" + err.getMessage + "_").send
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
          case Left(Some(e)) => sendErrorMessage(new Error(e))
          case Right(message) => message.send
          case Left(None) => ;
          case _ => sendErrorMessage(new Error("Unknown error"))
        }
        case Failure(e) => e match {
          case _: NoSuchElementException => ;
          case _ => sendErrorMessage(e)
        }
      }
    }
  }

  def main(args: Array[String]): Unit =
    listenEvent().failed.map(sendErrorMessage)
}

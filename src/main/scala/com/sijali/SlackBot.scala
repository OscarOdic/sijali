package com.sijali

import akka.actor.{ActorRef, ActorSystem}
import com.sijali.util.BotMessage
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
  def sendErrorMessage(err: Throwable): Unit = {
    val error = "[" + Console.RED + "error" + Console.RESET + "] "
    Console.err.println(error + err.getMessage)
    err.getStackTrace.foreach {s =>
      Console.err.println(error + "at " +  s.toString)
    }
    util.getImIdByUserId(ConfigFactory.load.getString("admin.id")) onSuccess {
      case channel => BotMessage(channel, "*[error]* _" + err.getMessage + "_").send
    }
  }

  def printInfoMessage(m: Message): Unit = {
    val info = "[" + Console.BLUE + "info" + Console.RESET + "] "
    println(info + "Received new message")
    println(info + m.toString)
  }

  /** Listen all slack event received by bot and execute handlers
    *
    * @return The ActorRef of the bot
    */
  def listenEvent(): Try[ActorRef] = Try {
    rtmClient.onMessage { m =>
      printInfoMessage(m)
      Future.sequence(handlerMessage(m)) onComplete {
        case Success(l) => l.foreach {
          case Left(Some(e)) => sendErrorMessage(new Error(e))
          case Right(message) => message.send
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
package com.sijali

import com.sijali.commands.Execution
import com.sijali.handlers.models.{Config, Reaction}
import com.sijali.util._
import com.typesafe.config.ConfigFactory
import pureconfig._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Random
import slack.models.Message

/** Handlers of slack events */
package object handlers {

  /** Send a reaction for a message
    *
    * @param message The message received by the bot
    * @param reaction The reaction of the bot
    *
    * @return a message to send by the bot
    */
  def genReactionMessage(message: Message, reaction: Reaction): BotMessage = {
    val response = if (reaction.response.length == 1)
      reaction.response.head
    else
      Random.shuffle(reaction.response).head
    if (response.params.isDefined) {
      val params = response.params.get
      BotMessage(
        channelId = message.channel,
        message = response.message.replaceAll("\\$<user>", "<@" + message.user + ">"),
        asUser = params.asUser,
        username = params.username,
        iconUrl = params.iconUrl,
        iconEmoji = params.iconEmoji
      )
    }
    else {
      BotMessage(
        channelId = message.channel,
        message = response.message.replaceAll("\\$<user>", "<@" + message.user + ">")
      )
    }
  }

  /** Send a private message to the admin slack user
    *
    * @param message The message received by the bot
    *
    * @return A possibly future message to admin user
    */
  def genPrivatesMessagesToAdmin(message: Message): Option[Future[BotMessage]] = {
    val adminId = ConfigFactory.load().getString("admin.id")

    if (message.user != adminId) {
      Some(getUserById(message.user) flatMap (user => {
        getImIdByUserId(adminId) map (im => {
          BotMessage(
            channelId = im,
            message = message.text,
            username = Some(user.name),
            iconUrl = user.profile.map(_.image_72)
          )
        })
      }))
    }
    else None
  }

  /** Send a private message to the admin slack user if the user send a message to the bot
    *
    * @param message The message received by the bot
    *
    * @return a probably future message
    */
  def checkPrivatesMessagesToAdmin(message: Message): Option[Future[Execution]] = {
    if (message.channel.startsWith("D")) {
      genPrivatesMessagesToAdmin(message) match {
        case None => None
        case Some(m) => Some(m.map(Right(_)))
      }
    } else None
  }

  /** Generate a reaction
    *
    * @param message The message received by the bot
    * @param reaction The reaction of the bot for this message
    */
  def genReaction(message: Message, reaction: Reaction): Future[Option[BotMessage]] = {
    val chanName = if (reaction.condition.channel == "") Left(true)
    else if (message.channel.head == 'C')
      Right(getChannelById(message.channel).map(_.name))
    else if (message.channel.head == 'G')
      Right(getGroupById(message.channel).map(_.name))
    else Left(false)

    chanName match {
      case Left(b) if b =>
          Future(Some(genReactionMessage(message, reaction)))
      case Right(c) =>
        c.map(n => {
          if (n == reaction.condition.channel) Some(genReactionMessage(message, reaction))
          else None
        })
      case _ => Future(None)
    }
  }

  /** Match if the bot have to react of a received message
    *
    * @param message The message received by the bot
    * @param reactions The list of possible reactions
    *
    * @return a list of future message or future error
    */
  def receivedMessageReaction(message: Message, reactions: List[Reaction]): List[Future[Option[BotMessage]]] = {
    reactions.filter(reaction => {
      val condition = reaction.condition

      val user = SlackBot.rtmClient.state
        .getUserById(message.user)
        .get

      val messageCondition = if (condition.not) {
        condition.message.r.findFirstIn(message.text.toLowerCase).isEmpty
      }
      else {
        condition.message.r.findFirstIn(message.text.toLowerCase).isDefined
      }

      val userCondition = if (condition.notUser) {
        condition.user.r.findFirstIn(user.name.toLowerCase).isEmpty
      }
      else {
        condition.user.r.findFirstIn(user.name.toLowerCase).isDefined
      }

      messageCondition && userCondition
    }).map(genReaction(message, _))
  }

  /** Execute a command
    *
    * @param message The message received by the bot
    * @param bannedUsers The banned user to use bot commands
    *
    * @return a probably future message
    */
  def executeCommand(
                      message: Message,
                      bannedUsers: Array[AnyRef]
                    ): Option[Future[Execution]] =
    if (bannedUsers.contains(message.user))
      Some(getImIdByUserId(message.user) map (im =>
        Right(BotMessage(
          channelId = im,
          message = ConfigFactory.load().getString("ban.message")
        ))
      ))
    else
      commands.execute(message.text.split(" ")(1), message.text.split(" ").drop(2), message.channel)

  /** Execute the reactions motor
    *
    * @param message The message received by the bot
    *
    * @return The list of futures messages
    */
  def executeReactions(message: Message): List[Future[Execution]] =
    receivedMessageReaction(message, loadConfig[Config].get.reactions).map(_ map {
      case Some(m) => Right(m)
      case None => Left(None)
    })

  /** Execute a command or a bot reaction
    *
    * @param message The message received by the bot
    *
    * @return The list of futures messages
    */
  def messageExecution(message: Message, bannedUsers: Array[AnyRef]): List[Future[Execution]] =
    if (message.text.startsWith(s"${SlackBot.botName} ")) List(executeCommand(message, bannedUsers)).flatten
    else executeReactions(message)

  /** Handler for a message
    *
    * @example send private bot messages to admin
    *          execute a bot command
    *          generate a reaction
    *
    * @param message The message received by the bot
    * @param bannedUsers The banned user to use bot commands
    *
    * @return The list of futures, for each future, a message to send, or an error
    */
  def handlerMessage(
                      message: Message,
                      bannedUsers: Array[AnyRef] = ConfigFactory.load()
                        .getStringList("ban.users").toArray
                    ): List[Future[Execution]] =
    if (SlackBot.botId != message.user)
      List(checkPrivatesMessagesToAdmin(message)).flatten ++ messageExecution(message, bannedUsers)
    else List.empty
}

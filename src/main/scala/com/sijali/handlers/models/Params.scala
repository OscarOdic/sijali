package com.sijali.handlers.models

/** Parameters for bot messages
  *
  * @param asUser If the bot is a user (with his defaults slack parameters
  * @param username The name of the bot
  * @param iconUrl The avatar of bot with url
  * @param iconEmoji The avatar of the bot with emoji
  */
case class Params (
                    asUser: Option[Boolean] = None,
                    username: Option[String] = None,
                    iconUrl: Option[String] = None,
                    iconEmoji : Option[String] = None
                  )

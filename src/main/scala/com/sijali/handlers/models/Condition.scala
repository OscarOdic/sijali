package com.sijali.handlers.models

/** A condition for bot reaction on slack
  *
  * @param message regex on received message
  * @param user regex on user who send the message
  * @param channel regex on channel where we received the message
  * @param not If is true, we try to match the negation of the message regex
  * @param notUser It is true, we try to match the negation of the user
  */
case class Condition (
                     message: String,
                     user: String = "",
                     channel: String = "",
                     not: Boolean=false,
                     notUser: Boolean=false
                     )

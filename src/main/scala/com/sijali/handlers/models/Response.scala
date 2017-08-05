package com.sijali.handlers.models

/** A Response of a reaction
  *
  * @param message The message to send
  * @param params Some optionals parameters for the message
  */
case class Response (
                    message: String,
                    params: Option[Params] = None
                    )

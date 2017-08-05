package com.sijali.handlers.models

/** A bot reaction (contain a condition and a response)
  *
  * @param condition The condition to have reaction
  * @param response The response of the reaction
  */
case class Reaction (
                    condition: Condition,
                    response: List[Response]
                    )

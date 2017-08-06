package com.sijali.util

import com.sijali.SlackBot._
import scala.concurrent.{ExecutionContext, Future}
import slack.models.{Channel, Group, User}

object Slack {
  implicit val executionContext: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global.prepare()

  /** Try the option function, if it is none, try to do the future function, and return a function from
    * string to future
    *
    * @param opt The option function
    * @param fut the future function
    * @tparam A The type of the returned future
    * @return The function from string to A future
    */
  private def getAById[A](opt: String => Option[A], fut: String => Future[A])(id: String): Future[A] =
    opt(id) match {
      case None => fut(id)
      case Some(c) => Future(c)
    }

  /** Get a channel with an id
    *
    * @return a function from string to a channel future
    */
  def getChannelById: String => Future[Channel] =
    getAById(
      id => rtmClient.getState.channels.find(_.id == id),
      id => apiClient.getChannelInfo(id)
    )

  /** get a channel id with a name
    *
    * @return a function from a string to a future of either a channel id or an error
    */
  def getChannelIdByName: String => Future[Either[String, String]] =
    getAById(
      name => rtmClient.getState.getChannelIdForName(name).map(Right(_)),
      name => apiClient.listChannels().map(_.find(_.name == name) match {
        case None => Left(s"Channel $name not found")
        case Some(c) => Right(c.id)
      })
    )

  /** get a channel with a name
    *
    * @return a function from a string to a future of either a channel or an error
    */
  def getChannelByName: String => Future[Either[String, Channel]] =
    getAById(
      name => rtmClient.getState.channels.find(_.name == name).map(Right(_)),
      name => apiClient.listChannels().map(_.find(_.name == name) match {
        case None => Left(s"Channel $name not found")
        case Some(c) => Right(c)
      })
    )

  /** Get a user with an id
    *
    * @return a function from string to a user future
    */
  def getUserById: String => Future[User] =
    getAById(
      id => rtmClient.getState.getUserById(id),
      id => apiClient.getUserInfo(id)
    )

  /** get a user id with a name
    *
    * @return a function from a string to a future of either a user id or an error
    */
  def getUserIdByName: String => Future[Either[String, String]] =
    getAById(
      name => rtmClient.getState.getUserIdForName(name).map(Right(_)),
      name => apiClient.listUsers().map(_.find(_.name == name) match {
        case None => Left(s"User $name not found")
        case Some(u) => Right(u.id)
      })
    )

  /** get a user with a name
    *
    * @return a function from a string to a future of either a user or an error
    */
  def getUserByName: String => Future[Either[String, User]] =
    getAById(
      name => rtmClient.getState.users.find(_.name == name).map(Right(_)),
      name => apiClient.listUsers().map(_.find(_.name == name) match {
        case None => Left(s"User $name not found")
        case Some(u) => Right(u)
      })
    )

  /** Get a private channel id with a user id
    *
    * @return a function from string to a private channel id
    */
  def getImIdByUserId: String => Future[String] =
    getAById(
      id => rtmClient.getState.ims.find(_.user == id).map(_.id),
      id => apiClient.openIm(id)
    )


  /** Get a private channel id with a user name
    *
    * @return a function from string to either a private channel id or an error
    */
  def getImIdByUserName(name: String): Future[Either[String, String]] =
    getUserIdByName(name) flatMap {
      case Left(e) => Future(Left(e))
      case Right(u) => getImIdByUserId(u).map(Right(_))
    }

  /** Get a private group with an id
    *
    * @return a function from string to a group future
    */
  def getGroupById: String => Future[Group] =
    getAById(
      id => rtmClient.getState.groups.find(_.id == id),
      id => apiClient.getGroupInfo(id)
    )

  /** get a private group id with a name
    *
    * @return a function from a string to a future of either a group id or an error
    */
  def getGroupIdByName: String => Future[Either[String, String]] =
    getAById(
      name => rtmClient.getState.groups.find(_.name == name).map(g => Right(g.id)),
      name => apiClient.listGroups().map(_.find(_.name == name) match {
        case None => Left(s"Group $name not found")
        case Some(g) => Right(g.id)
      })
    )

  /** get a group with a name
    *
    * @return a function from a string to a future of either a private group or an error
    */
  def getGroupByName: String => Future[Either[String, Group]] =
    getAById(
      name => rtmClient.getState.groups.find(_.name == name).map(Right(_)),
      name => apiClient.listGroups().map(_.find(_.name == name) match {
        case None => Left(s"Group $name not found")
        case Some(c) => Right(c)
      })
    )

  /** get a chan id (channel, private group, or private channel) with a name
    *
    * @return a function from a string to a future of either a chan id or an error
    */
  def getChanIdByName: String => Future[Either[String, String]] =
    getAById(
      name => rtmClient.getState.getChannelIdForName(name).map(Right(_)).orElse(
        rtmClient.getState.ims.find(_.user == rtmClient.getState.getUserIdForName(name)).map(u => Right(u.id)).orElse(
          rtmClient.getState.groups.find(_.name == name).map(g => Right(g.id)))),
      name => apiClient.listChannels().map(_.find(_.name == name).map(_.id)) flatMap {
        case None => apiClient.listUsers().map(_.find(_.name == name).map(_.id)) flatMap {
          case None => apiClient.listGroups().map(_.find(_.name == name).map(_.id))
          case Some(u) => apiClient.openIm(u).map(Some(_))
        }
        case c => Future(c)
      } map {
        case None => Left(s"Channel, User or Group $name not found")
        case Some(chanId) => Right(chanId)
      }
    )
}

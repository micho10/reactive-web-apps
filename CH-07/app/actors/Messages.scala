package actors

import org.joda.time.DateTime

/**
  * Created by carlos on 26/01/17.
  */
trait Command {
  val phoneNumber: String
}
case class RegisterUser(phoneNumber: String, userName: String) extends Command
case class SubscribeMentions(phoneNumber: String) extends Command
case class ConnectUser(phoneNumber: String) extends Command


trait Event {
  val timestamp: DateTime
}
case class UserRegistered(phoneNumber: String, userName: String, timestamp: DateTime = DateTime.now) extends Event
case class MentionSubscribed(timestamp: DateTime = DateTime.now) extends Event
case class MentionReceived(id: String, created_on: DateTime, from: String, text: String,
                           timestamp: DateTime = DateTime.now) extends Event
case class MentionAcknowledged(id: String, timestamp: DateTime = DateTime.now) extends Event
case class ClientEvent(phoneNumber: String, userName: String, event: Event, timestamp: DateTime = DateTime.now)
  extends Event


trait Query
case class MentionsToday(phoneNumber: String) extends Query
case class MentionsPastWeek(phoneNumber: String) extends Query


trait QueryResult
case class DailyMentionsCount(count: Int) extends QueryResult
case class WeeklyMentionsCount(count: Int) extends QueryResult
case object QueryFailed extends QueryResult


// This message is sent back directly without phone number, so we don't extend our Command here
case class AcknowledgeMention(id: String)
case class UnknownUser(phoneNumber: String)
case class InvalidCommand(reason: String)

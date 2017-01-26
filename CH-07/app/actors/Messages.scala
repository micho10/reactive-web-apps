package actors

import org.joda.time.DateTime

/**
  * Created by carlos on 26/01/17.
  */
trait Command {
  val phoneNumber: String
}



trait Event {
  val timestamp: DateTime
}



case class RegisterUser(phoneNumber: String, userName: String) extends Command
case class Unregistered(phoneNumber: String, userName: String, timestamp: DateTime = DateTime.now) extends Event

case class InvalidCommand(reason: String)

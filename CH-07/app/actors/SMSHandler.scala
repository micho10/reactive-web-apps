package actors

import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.io.Tcp.{PeerClosed, Received, Write}
import akka.util.{ByteString, Timeout}

/**
  * Created by carlos on 25/01/17.
  */
class SMSHandler(connection: ActorRef) extends Actor with ActorLogging {

  implicit val timeout = Timeout(2.seconds)
  implicit val ec = context.dispatcher

  lazy val commandHandler = context.actorSelection("akka://application/user/sms/commandHandler")

  // Declares the pattern for matching incoming messages
  val MessagePattern = """[\+]([0-9]*) (.*)""".r
  // Declares the pattern for matching a valid registration command
  val RegistrationPattern = """register (.*)""".r


  override def receive = {
    // Handles the reception of data
    case Received(data) =>
      // Prints out the received data (encoded as ByteString), assuming it's a UTF-8 String
      log.info("Received message: {}", data.utf8String)
      data.utf8String.trim match {
        case MessagePattern(number, message) =>
          message match {
            // Sends a RegisterUser command to the command handler
            case RegistrationPattern(userName) => commandHandler ! RegisterUser(number, userName)
          }

        case other =>
          log.warning("Invalid message {}", other)
          sender() ! Write(ByteString("Invalid message format\n"))
      }

      // Echoes the incoming message back to the connection
      connection ! Write(data)

    // Answers with success if the registration succeeded
    case registered: UserRegistered => connection ! Write(ByteString("Registration successful\n"))

    // Relays results of invalid commands
    case InvalidCommand(reason) => connection ! Write(ByteString(reason + "\n"))

    // Handles the disconnection of the client
    case PeerClosed => context stop self
  }

}

package actors

import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.io.Tcp.{PeerClosed, Received, Write}

/**
  * Created by carlos on 25/01/17.
  */
class SMSHandler(connection: ActorRef) extends Actor with ActorLogging {

  override def receive = {
    // Handles the reception of data
    case Received(data) =>
      // Prints out the received data (encoded as ByteString), assuming it's a UTF-8 String
      log.info("Received message: {}", data.utf8String)
      // Echoes the incoming message back to the connection
      connection ! Write(data)

    // Handles the disconnection of the client
    case PeerClosed => context stop self
  }

}

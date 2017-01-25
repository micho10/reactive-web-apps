package actors

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorLogging, Props}
import akka.io.{IO, Tcp}
import akka.io.Tcp._

/**
  * Created by carlos on 25/01/17.
  */
class SMSServer extends Actor with ActorLogging {

  // Imports the ActorSystem required by Akka IO
  import context.system

  // Instructs Akka IO to bind to the socket on localhost at port 6666
  IO(Tcp) ! Bind(self, new InetSocketAddress("localhost", 6666))

  override def receive = {
    // Handles the case when the socket was successfully bound
    case Bound(localAddress) => log.info("SMS server listening on {}", localAddress)

    // Handles the case when the socket couldn't be bound by giving up
    case CommandFailed(_: Bind) => context stop self

    case Connected(remote, local) =>
      val connection = sender()
      // Sets up a new handler for the client connection by creating a child SMSHandler and passing it
      // the client connection
      val handler = context.actorOf(Props(classOf[SMSHandler], connection))
      // Registers the handler with the Akka IO subsystem
      connection ! Register(handler)
  }

}

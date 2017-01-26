package actors

import akka.actor.Actor.Receive
import akka.actor.{ActorLogging, Props}

/**
  * Created by carlos on 26/01/17.
  */
class CQRSCommandHandler extends PersistentActor with ActorLogging {

  override def persistenceId: String = "CQRSCommandHandler"


  override def receiveRecover: Receive = {
    // Handles the failure of recovery by logging it out
    case RecoveryFailure(cause) => log.error(cause, "Failed to recover")
    // Handles the end of recovery
    case RecoveryCompleted      => log.info("Recovery completed")
    // Handles events that are replayed during recovery
    case evt: Event             => handleEvent(evt)
  }


  override def receiveCommand: Receive = {
    // Persists the registration of a user as a UserRegistered event, and calls the handleEvent function in the callback
    case RegisterUser(phoneNumber, userName)  => persist(completed(phoneNumber, userName))(handleEvent)
    case command: Command                     =>
      context.child(command.phoneNumber).map { reference =>
        reference forward command
      } getOrElse {
        // Returns an error if the phone number is unknown, which is when there's no child actor with that identifier
        sender() ! "User unknown"
      }
  }


  def handleEvent(event: Event, recovery: Boolean): Unit = event match {
    case registered @ UserRegistered(phoneNumber, userName, _) =>
      // Creates the ClientCommandHandler as a child actor
      context.actorOf(
        // Passes the phone number and user name as constructor paramenters to the ClientCommandHandler
        props = Props(classOf[ClientCommandHandler], phoneNumber, userName),
        name = phoneNumber)
      if (recoveryFinished)
        // Informs the client that registration worked if you're not in recovery
        sender() ! registered
  }

}

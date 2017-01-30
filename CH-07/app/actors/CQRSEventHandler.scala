package actors

import akka.actor.{Actor, ActorLogging}
import play.api.db.Database

/**
  * Created by carlos on 30/01/17.
  */
class CQRSEventHandler(database: Database) extends Actor with ActorLogging {

  override def preStart(): Unit = {
    // Subscribes to all messages that match the Event trait and delivers them to this actor
    context.system.eventStream.subscribe(self, classOf[Event])
  }

  override def receive = ???

}

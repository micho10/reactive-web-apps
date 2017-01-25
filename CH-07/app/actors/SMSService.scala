package actors

import javax.inject.Inject

import akka.actor.{Actor, ActorLogging}
import com.google.inject.AbstractModule
import play.api.db.Database
import play.api.libs.concurrent.AkkaGuiceSupport

/**
  * Uses dependency injection to wire dependencies in the actor's constructor
  *
  * Created by carlos on 25/01/17.
  */
class SMSService @Inject()(database: Database) extends Actor with ActorLogging {

  override def receive = ???

}


/**
  * Mixes in the AkkaGuiceSupport trait to provide the dependency injection tooling for actors
  */
class SMSServiceModule extends AbstractModule with AkkaGuiceSupport {

  // Declares the binding for the SMSService actor with the name "sms".
  // The name will be used for naming the binding as well as the actor.
  def configure(): Unit = bindActor[SMSService]("sms")

}

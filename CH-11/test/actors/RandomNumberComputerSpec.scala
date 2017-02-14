package actors

import actors.RandomNumberComputer.{ComputeRandomNumber, RandomNumber}
import akka.actor.ActorSystem
import akka.testkit._

import scala.concurrent.duration._
import org.scalatest._

/**
  * Extends the TestKit class that provides testing functionality
  *
  * Mixes in implicit sender behaviour that sets the test actor of the TestKit to be the target of sent messages
  *
  * Mixes in the FlatSpec behaviour using the FlatSpecLike trait
  *
  * Tells ScalaTest that we'd like support for optionally executing custom code before and after all classes.
  *
  * Created by carlos on 14/02/17.
  */
class RandomNumberComputerSpec(_system: ActorSystem) extends TestKit(_system) with ImplicitSender with FlatSpecLike
  with ShouldMatchers with BeforeAndAfterAll {

  // Defines a default constructor that provides an ActorSystem
  def this() = this(ActorSystem("RandomNumberComputerSpec"))

  override def afterAll: Unit = {
    // Shuts down the ActorSystem after all cases have run
    TestKit.shutdownActorSystem(system)
  }

  "A RandomNumberComputerSpec" should "send back a random number" in {
    // Initializes tha actor we'd like to test
    val randomNumberComputer = system.actorOf(RandomNumberComputer.props)
    // Uses the TestKit's within method to check whether we get a result within 100 ms,
    // taking into account optional time scaling
    within(100.millis.dilated) {
      randomNumberComputer ! ComputeRandomNumber(100)
      // Expects a message of type RandomNumber to be returned (we don't know which number it will be)
      expectMsgType[RandomNumber]
    }
  }

}

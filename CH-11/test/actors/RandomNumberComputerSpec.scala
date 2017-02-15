package actors

import actors.RandomNumberComputer.{ComputeRandomNumber, RandomNumber}
import akka.actor.SupervisorStrategy.Restart
import akka.actor.{Actor, ActorRef, ActorSystem, OneForOneStrategy, Props, SupervisorStrategy}
import akka.testkit._
import com.typesafe.config.ConfigFactory

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
  def this() = this(ActorSystem(
    "RandomNumberComputerSpec",
    // Customize the scaling factor
    ConfigFactory.parseString(
      s"akka.test.timefactor = " + sys.props.getOrElse("SCALING_FACTOR", default = "1.0")
    )
  ))

  override def afterAll = {
    // Shuts down the ActorSystem after all cases have run
    TestKit.shutdownActorSystem(system)
  }

  /**
    * Test an Actor for responsiveness
    */
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

  /**
    * Test an Actor for resilience using the StepParent pattern
    */
  it should "fall when the maximum is a negative number" in {

    class StepParent(target: ActorRef) extends Actor {
      override def supervisorStrategy: SupervisorStrategy =
        OneForOneStrategy() {
          case t: Throwable =>
            // Communicates failures to the target actor by sending them
            target ! t
            Restart
        }
      override def receive = {
        // Creates a child actor when receiving its Props and sending back its reference
        case props: Props => sender ! context.actorOf(props)
      }
    }

    val parent = system.actorOf(
      // Creates a StepParent actor and passes in the testActor as target for communication
      Props(new StepParent(testActor)), name = "stepParent"
    )
    // Sends the StepParent the Props of the actor we want to test (the RandomNumberComputer)
    parent ! RandomNumberComputer.props
    // Retrieves the reference to the actor we want to test by expecting a message of type ActorRef
    val actorUnderTest = expectMsgType[ActorRef]
    // Tests the RandomNumberComputer with a message that should provoke a failure
    actorUnderTest ! ComputeRandomNumber(-1)
    // Checks whether the actor did indeed fail by expecting an IllegalArgumentException
    expectMsgType[IllegalArgumentException]
  }

}

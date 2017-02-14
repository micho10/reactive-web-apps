package actors

import actors.RandomNumberComputer.{ComputeRandomNumber, RandomNumber}
import akka.actor.{Actor, Props}

import scala.util.Random

/**
  * Created by carlos on 14/02/17.
  */
class RandomNumberComputer extends Actor {

  override def receive = {
    case ComputeRandomNumber(max) =>
      // Returns a random number in the range from 0 to max when asked
      sender() ! RandomNumber(Random.nextInt(max))
  }

}



object RandomNumberComputer {
  // Defines a helper method for creating the props
  def props = Props[RandomNumberComputer]

  case class ComputeRandomNumber(max: Int)
  case class RandomNumber(n: Int)
}

package services

import scala.concurrent.Future
import scala.util.control.NonFatal
import scala.concurrent.ExecutionContext.Implicits._

/**
  * Defines our component as a trait to ease testing
  *
  * Created by carlos on 14/02/17.
  */
trait RandomNumberService {
  def generateRandomNumber: Future[Int]
}



/**
  * Defines an implementation of our component depending on a DiceService
  *
  * @param dice
  */
class DiceDrivenRandomNumberService(dice: DiceService) extends RandomNumberService {
  // Recovers failure using the recoverWith handler
  override def generateRandomNumber: Future[Int] = dice.throwDice.recoverWith {
    // Simply invokes the method again until it works
    case NonFatal(t) => generateRandomNumber
  }
}

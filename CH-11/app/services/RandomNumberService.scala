package services

import scala.concurrent.Future

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
  override def generateRandomNumber: Future[Int] = dice.throwDice
}



/**
  * Defines the DiceService implementation as a trait as well
  */
trait DiceService {
  def throwDice: Future[Int]
}



/**
  * Defines a simple but powerful implementation of a DiceService
  */
class RollingDiceService extends DiceService {
  override def throwDice: Future[Int] =
    Future.successful {
      4 // chosen by fair dice roll.
        // guaranteed to be random.
    }
}

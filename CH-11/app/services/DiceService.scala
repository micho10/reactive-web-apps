package services

import scala.concurrent.Future

/**
  * Defines the DiceService implementation as a trait as well
  *
  * Created by carlos on 14/02/17.
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

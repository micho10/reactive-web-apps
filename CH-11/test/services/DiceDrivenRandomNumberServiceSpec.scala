package services

import java.util.concurrent.atomic.AtomicInteger

import org.scalatest.time.{Millis, Span}
import org.scalatest.{FlatSpec, ShouldMatchers}
import org.scalatest.concurrent.ScalaFutures

import scala.concurrent.Future

/**
  * Uses the FlatSpec specification style that allows you to define one case after another
  *
  * Mixes in the ScalaFutures trait that provides support for futures
  *
  * Uses ShouldMatchers as a flavor for expressing assertions
  *
  * Created by carlos on 14/02/17.
  */
class DiceDrivenRandomNumberServiceSpec extends FlatSpec with ScalaFutures with ShouldMatchers {

  "The DiceDrivenRandomNumberService" should "return a number provided by a dice" in {

    // Provides a custom PatienceConfig
    implicit val patienceConfig = PatienceConfig(
      // Specifies how much time a future will be given to succeed before giving up
      timeout = scaled(Span(150, Millis)),
      // Specifies how much time to wait between checks to determine success when polling
      interval = scaled(Span(15, Millis))
    )

    // Implements a simple DiceService to know exactly what to expect
    val diceService = new DiceService {
      override def throwDice: Future[Int] = Future.successful(4)
    }

    // Instantiates the RandomNumberService we want to test
    val randomNumberService = new DiceDrivenRandomNumberService(diceService)

    // Invokes the service method we want to test and passes it to ScalaTest's whenReady function
    whenReady(randomNumberService.generateRandomNumber) { result =>
      // Verifies the correctness of the result
      result shouldBe 4
    }
  }


  it should "be able to cope with problematic dice throws" in {
    // Implements a DiceService that will fail one time out of two, including the first time it's executed
    val overzealousDiceThrowingService = new DiceService {
      val counter = new AtomicInteger()
      override def throwDice: Future[Int] = {
        val count = counter.incrementAndGet()
        if (count % 2 == 0) Future.successful(4)
        else Future.failed(new RuntimeException("Dice fell off the table and the cat won't give it back"))
      }
    }

    val randomNumberService = new DiceDrivenRandomNumberService(overzealousDiceThrowingService)

    whenReady(randomNumberService.generateRandomNumber) { result =>
      // Expects to receive a result nonetheless
      result shouldBe 4
    }
  }

}

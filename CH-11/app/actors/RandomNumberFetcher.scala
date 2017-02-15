package actors

import actors.RandomNumberFetcher.{FetchRandomNumber, RandomNumber}
import akka.actor.{Actor, Props}
import akka.pattern.pipe
import play.api.libs.json.{JsArray, Json}
import play.api.libs.ws.WSClient

import scala.concurrent.Future

/**
  * Created by carlos on 15/02/17.
  */
class RandomNumberFetcher(ws: WSClient) extends Actor {

  implicit val ec = context.dispatcher

  override def receive = {
    // Pipes the result of the future call to RANDOM.ORG to the sender, requesting a random number
    case FetchRandomNumber(max) => fetchRandomNumber(max).map(RandomNumber) pipeTo sender()
  }

  def fetchRandomNumber(max: Int): Future[Int] =
    ws.url("https://api.random.org/json-rpc/1/invoke")
      .post(Json.obj(
        "jsonrpc" -> "2.0",
        "method"  -> "generateIntegers",
        "params"  -> Json.obj(
          // Passes the API key
          "apiKey"      -> "...",
          "n"           -> 1,
          "min"         -> 0,
          "max"         -> max,
          "replacement" -> true,
          "base"        -> 10
        ),
        "id"      -> 42
      )).map { response =>
      // Extracts the result in an unsafe manner to trigger a failure of the future if anything goes wrong
      (response.json \ "result" \ "random" \ "data")
        .as[JsArray]
        .value
        .head
        .as[Int]
    }

}



object RandomNumberFetcher {
  def props(ws: WSClient) = Props(classOf[RandomNumberFetcher], ws)

  case class FetchRandomNumber(max: Int)
  case class RandomNumber(n: Int)
}
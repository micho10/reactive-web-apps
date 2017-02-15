package controllers

import javax.inject.Inject

import actors.RandomNumberFetcher
import actors.RandomNumberFetcher.{FetchRandomNumber, RandomNumber}
import akka.actor.ActorSystem
import akka.util.Timeout
import play.api.libs.ws.WSClient
import play.api.mvc.{Action, Controller}

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext

/**
  * Wires in dependencies using dependency injection
  *
  * @param ws
  * @param ec
  * @param system
  */
class Application @Inject()(ws: WSClient, ec: ExecutionContext, system: ActorSystem)  extends Controller {

  implicit val executionContext = ec
  // Sets timeout at 2 seconds
  implicit val timeout = Timeout(2000.millis)

  // Creates one single RandomNumberFetcher actor
  val fetcher = system.actorOf(RandomNumberFetcher.props(ws))

  def index = Action { implicit request =>
    Ok(views.html.index())
  }

  def compute = Action.async { implicit request =>
    (fetcher ? FetchRandomNumber(10)).map {
      case RandomNumber(r) => Redirecct(routes.Application.index())
          .flashing("result" -> s"The result is $r")

      case other =>
        // If we don't get a RandomNumber back, simply fails
        InternalServerError
    }
  }

}

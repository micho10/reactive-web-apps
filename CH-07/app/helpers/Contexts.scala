package helpers

import play.api.libs.concurrent.Akka

import scala.concurrent.ExecutionContext

/**
  * Created by carlos on 19/01/17.
  */
object Contexts {

  val database: ExecutionContext = Akka.system.dispatchers.lookup("contexts.database")

}

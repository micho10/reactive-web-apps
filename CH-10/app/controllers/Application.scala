package controllers

import javax.inject.Inject

import play.api.Configuration
import play.api.mvc._

class Application @Inject()(configuration: Configuration) extends Controller {

  def index = Action { implicit request =>
    Ok(views.html.index("Your new application is ready."))
  }

  def text = Action {
    Ok(configuration.getString("text").getOrElse("Hello world!"))
  }

}

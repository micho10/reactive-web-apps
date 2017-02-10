import org.scalatestplus.play._

/**
  * Extends the PlaySpecification, which provides Play-specific context to a ScalaTest spec.
  *
  * Uses the same test Play server for the whole test suite
  *
  * Uses the same browser instance for the whole test suite
  *
  * Uses Firefox as a web browser
  */
class ApplicationSpec extends PlaySpec with OneServerPerSuite with OneBrowserPerSuite with FirefoxFactory {

  "The Application" must {
    "display a text when clicking on a button" in {
      go to (s"http://localhost:$port")
      pageTitle mustBe "Hello"
      // Clicks on the button
      click on find(id("button")).value
      // Tells ScalaTest that this check isn't immediate since asynchronous behaviour is involved
      eventually {
        // Tests if the displayed text corresponds to the expected one from the configuration file
        find(id("text")).map(_.text) mustBe app.configuration.getString("text")
      }
    }
  }
}

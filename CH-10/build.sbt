name := """CH-10"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(
  PlayScala,
  DebianPlugin,
  JavaServerAppPackaging
)

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.webjars"             %% "webjars-play"             % "2.4.0-1",
  "org.webjars"              %  "jquery"                  % "2.1.4",
  "org.seleniumhq.selenium"  % "selenium-firefox-driver"  % "3.0.1",
  "org.scalatest"           %% "scalatest"                % "2.2.1"     % "test",
  "org.scalatestplus"       %% "play"                     % "1.4.0-M4"  % "test"
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator

// Adds rjs as first stage of the assets pipeline
pipelineStages := Seq(rjs)

// Specifies that the main RequireJS module is called "application" (this will resolve to our application.js file)
RjsKeys.mainModule := "application"

// Specifies that the configuration for RequireJS is located in the application module
RjsKeys.mainConfig := "application"

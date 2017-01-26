name := """CH-07"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"

resolvers += "spy Repository" at "http://files.couchbase.com/maven2"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  specs2 % Test,
  "com.github.mumoshu"  %% "play2-memcached-play24"         % "0.7.0",
  "org.postgresql"       % "postgresql"                     % "9.4-1201-jdbc41",
  "org.jooq"             % "jooq"                           % "3.7.0",
  "org.jooq"             % "jooq-codegen-maven"             % "3.7.0",
  "org.jooq"             % "jooq-meta"                      % "3.7.0",
  "com.ning"             % "async-http-client"              % "1.9.29",
  "joda-time"            % "joda-time"                      % "2.7",
  "com.github.ironfish" %% "akka-persistance-mongo-casbah"  % "0.7.6"
)

// Declares the generateJOOQ sbt task
val generateJOOQ = taskKey[Seq[File]]("Generate JooQ classes")

// Defines the implementation of the task, with dependencies on the context (base directory, classpath, and so on)
val generateJOOQTask = (baseDirectory, dependencyClasspath in Compile,
  runner in Compile, streams) map { (base, cp, r, s) =>
    // Runs the GenerationTool and provides the configuration file as an argument
    toError(r.run(
      "org.jooq.util.GenerationTool",
      cp.files,
      Array("conf/chapter7.xml"),
      s.log))
  // Returns the generated files so that you can use this task as an sbt source generator
  ((base / "app" / "generated") ** "*.scala").get
}

// Wires the implementation of the task to the task key
generateJOOQ <<= generateJOOQTask

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator

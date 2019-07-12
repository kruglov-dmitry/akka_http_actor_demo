name := "akka_http_actor_demo"
version := "1.0.0"
scalaVersion := "2.11.8"

libraryDependencies ++= {
	val akkaVersion	=	"2.5.16"
  val akkaHttpVersion = "10.1.5"
	val logbackVersion =	"1.2.3"
	val scalaLoggingVersion	=	"3.9.0"

	Seq(
	"com.typesafe.akka" %% "akka-actor"   % akkaVersion,
	"com.typesafe.akka" %% "akka-slf4j"   % akkaVersion,
  "com.typesafe.akka" %% "akka-stream"  % akkaVersion,
  "com.typesafe.akka" %% "akka-http"    % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "com.typesafe"      % "config" % "1.3.3",
  "ch.qos.logback"		% "logback-classic" % logbackVersion,
	"com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion,
  "com.typesafe.akka" %% "akka-http-testkit"  % akkaHttpVersion % Test,
  "org.scalatest"     %% "scalatest"          % "3.1.0-SNAP6" % Test
  )
}

fork in Test := true
javaOptions in Test ++= Seq("-Dconfig.file=conf/app.conf")

scalacOptions ++= Seq(
  "-Ywarn-dead-code",
  "-Ywarn-infer-any",
  "-Ywarn-unused-import",
  //"-Xfatal-warnings",
  "-Xlint"
)

scalastyleFailOnError := true
scalastyleFailOnWarning := true

// Create a default Scala style task to run with tests
lazy val compileScalastyle = taskKey[Unit]("compileScalastyle")

compileScalastyle := scalastyle.in(Compile).toTask("").value

(compileInputs in(Compile, compile)) := ((compileInputs in(Compile, compile)) dependsOn compileScalastyle).value
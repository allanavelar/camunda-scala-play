name := "camunda-scala-play"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
	"org.postgresql" % "postgresql" % "9.3-1102-jdbc41",
	"org.camunda.bpm" % "camunda-bom" % "7.2.0",
	"org.camunda.bpm" % "camunda-engine" % "7.2.0"
)

name := "LithePad"
version := "1.0"
scalaVersion := "2.11.6"
libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "1.0.1"
libraryDependencies += "de.sciss" % "jsyntaxpane" % "1.0.0"
mainClass in Compile := Some("TextApp")
assemblyJarName in assembly := "LithePad.jar"
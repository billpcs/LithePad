name := "LithePad"
version := "1.0"
scalaVersion := "2.11.8"
libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "1.0.1"
libraryDependencies += "de.sciss" % "syntaxpane" % "1.1.5"
libraryDependencies += "com.github.scopt" %% "scopt" % "3.5.0"
mainClass in Compile := Some("LithePad")
assemblyJarName in assembly := "LithePad.jar"
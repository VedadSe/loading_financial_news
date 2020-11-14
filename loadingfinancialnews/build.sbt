name := "loadingfinancialnews"

version := "0.1"

scalaVersion := "2.13.3"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.6.6"

assembly / test := {}
assembly / assemblyJarName := "newsloading.jar"
assembly / mainClass := Some("FinancialNewsLoading.NewsActorMainProgram")
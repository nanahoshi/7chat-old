name := "7chat"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.12.2"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)
  
libraryDependencies += guice
libraryDependencies += jdbc

// https://mvnrepository.com/artifact/org.postgresql/postgresql
libraryDependencies += "org.postgresql" % "postgresql" % "42.1.4"

libraryDependencies += "org.twitter4j" % "twitter4j-core" % "4.0.6" // for Twitter
// https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-core
//libraryDependencies += "com.fasterxml.jackson.core" % "jackson-core" % "2.9.2"

// https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind
//libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % "2.9.2"
// https://mvnrepository.com/artifact/org.apache.httpcomponents/httpclient
libraryDependencies += "org.apache.httpcomponents" % "httpclient" % "4.5.3"

libraryDependencies ++= Seq(
  "org.deeplearning4j" % "deeplearning4j-core" % "0.9.1",
  "org.nd4j" % "nd4j-native-platform" % "0.9.1",
/*  "org.nd4j" % "nd4j-cuda-8.0-platform" % "0.9.1", */
  "org.deeplearning4j" % "deeplearning4j-nlp" % "0.9.1"
)
// https://mvnrepository.com/artifact/com.ibm.watson.developer_cloud/personality-insights
libraryDependencies += "com.ibm.watson.developer_cloud" % "personality-insights" % "4.0.0"

libraryDependencies += "org.awaitility" % "awaitility" % "2.0.0" % Test
libraryDependencies += "org.assertj" % "assertj-core" % "3.6.2" % Test
libraryDependencies += "org.mockito" % "mockito-core" % "2.1.0" % Test
testOptions in Test += Tests.Argument(TestFrameworks.JUnit, "-a", "-v")

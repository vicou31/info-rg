import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._
import sbt._

object Dependencies {
  private val uPickleVersion = "0.7.1"
  private val autowireVersion = "0.2.6"
  private val sloggingVersion = "0.6.1"
  private val scalaTagsVersion = "0.6.7"
  private val scalaCssVersion = "0.5.3"
  private val scalajsDomVersion = "0.9.6"
  private val scalajsReactVersion = "1.4.0"
  private val scalajsJQueryVersion =  "0.9.1"

  object client {
    val jsDependencies = Def.setting(
      Seq(
        // Used to add classes to HTML elements and also remove them: https://mvnrepository.com/artifact/org.webjars/jquery
        "org.webjars" % "jquery" % "3.1.1"
          / "3.1.1/jquery.js"
          minified "jquery.min.js",

        "org.webjars.npm" % "react" % "16.7.0"
          / "umd/react.development.js"
          minified "umd/react.production.min.js"
          commonJSName "React",

        "org.webjars.npm" % "react-dom" % "16.7.0"
          / "umd/react-dom.development.js"
          minified "umd/react-dom.production.min.js"
          dependsOn "umd/react.development.js"
          commonJSName "ReactDOM",

        "org.webjars.npm" % "react-dom" % "16.7.0"
          / "umd/react-dom-server.browser.development.js"
          minified "umd/react-dom-server.browser.production.min.js"
          dependsOn "umd/react-dom.development.js"
          commonJSName "ReactDOMServer"
      )
    )
    val dependencyOverrides = "org.webjars.npm" % "js-tokens" % "4.0.0"
    
    val scalaJsDependencies = Def.setting(Seq(
        // Used to produce HTML and CSS with Scala on the client side: https://github.com/lihaoyi/scalatags
        "com.lihaoyi" %%% "scalatags" % scalaTagsVersion,
        "com.github.japgolly.scalacss" %%% "core" % scalaCssVersion,
        "com.github.japgolly.scalacss" %%% "ext-react" % "0.5.3",
        "com.github.japgolly.scalacss" %%% "ext-scalatags" % "0.5.3",

        // Serializes data between client and server: https://github.com/lihaoyi/upickle-pprint
        "com.lihaoyi" %%% "upickle" % uPickleVersion,

        // Type-safe Ajax calls between client and server: https://github.com/lihaoyi/autowire
        "com.lihaoyi" %%% "autowire" % autowireVersion,

        // A type facade for jQuery so we can use the JavaScript library in a type-safe manner: https://github.com/scala-js/scala-js-jquery
        "be.doeraene" %%% "scalajs-jquery" % scalajsJQueryVersion,

        // Logging: https://github.com/jokade/slogging
        "biz.enef" %%% "slogging" % sloggingVersion,

        "org.scala-js" %%% "scalajs-dom" % scalajsDomVersion,

        "com.github.japgolly.scalajs-react" %%% "core" % scalajsReactVersion,

        "com.github.japgolly.scalajs-react" %%% "extra" % scalajsReactVersion
      )
    )
  }

  val server = {
    // Akka HTTP has a dependency on akka-actor. This akka-actor version must match the one of akka-slf4j
    val akkaActorVersion = "2.5.21"
    // For the newest version see: http://doc.akka.io/docs/akka-http/current/scala/http/index.html
    val akkaHttpVersion = "10.1.7"
    Def.setting(Seq(
      // Our HTTP server: http://doc.akka.io/docs/akka-http/current/index.html
      // It's important that the Akka actor library used by Akka HTTP and by other dependencies such as Akka SLF4J is
      // the same, otherwise the application will throw a NoSuchMethodError at startup.
      "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,

      "com.typesafe.akka" %% "akka-stream" % "2.5.21",
      

      // The server creates HTML pages and CSS that it sends to the client
      "com.lihaoyi" %% "scalatags" % scalaTagsVersion,
      "com.github.japgolly.scalacss" %% "core" % scalaCssVersion,

      // Serializes data between client and server: https://github.com/lihaoyi/upickle-pprint
      "com.lihaoyi" %% "upickle" % uPickleVersion,
      // Type-safe Ajax calls between client and server: https://github.com/lihaoyi/autowire
      "com.lihaoyi" %% "autowire" % autowireVersion,

      // Logging --------------
      // Logging facade for Scala and Scala.js: https://github.com/jokade/slogging
      "biz.enef" %% "slogging-slf4j" % sloggingVersion,
      // Logging backend: http://logback.qos.ch/
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      // Akka, which provides our HTTP server, logs using SLF4J: https://mvnrepository.com/artifact/com.typesafe.akka/akka-slf4j_2.12
      "com.typesafe.akka" %% "akka-slf4j" % akkaActorVersion,
      // Needed for reading the logback.groovy configuration file
      "org.codehaus.groovy" % "groovy-all" % "2.4.7",

      // Database connection ---------------------

      // Hikari Connection Pool has a dependency on Slick
      // https://mvnrepository.com/artifact/com.typesafe.slick/slick-hikaricp_2.12
      "com.typesafe.slick" %% "slick-hikaricp" % "3.3.0",
      // https://mvnrepository.com/artifact/org.postgresql/postgresql
      "org.postgresql" % "postgresql" % "42.2.5",

      // Testing -------------------
      "org.scalatest" %% "scalatest" % "3.0.1" % "test",
      // Used to tests our routes, for example: https://mvnrepository.com/artifact/com.typesafe.akka/akka-http-testkit_2.12
      "com.typesafe.akka" %% "akka-http-testkit" % "10.1.7",
      "com.typesafe.akka" %% "akka-testkit" % "2.5.21" % Test,
      // For executing HTTP requests that test our application: https://github.com/scalaj/scalaj-http
      "org.scalaj" %% "scalaj-http" % "2.4.1" % "test",
      // Helps us verify our HTTP responses: https://github.com/ruippeixotog/scala-scraper
      "net.ruippeixotog" %% "scala-scraper" % "2.1.0" % "test"
    ))
  }
}

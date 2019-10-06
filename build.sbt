

scalaVersion in ThisBuild := ScalaConfig.version
version in ThisBuild := BuildConfig.appVersion

def inDevMode = sys.props.get("dev.mode").exists(value => value.equalsIgnoreCase("true"))
scalacOptions in ThisBuild := ScalaConfig.compilerOptions(inDevMode).value

lazy val root = project.in(file("."))
  .aggregate(client,server)
  .settings(
    name := s"${BuildConfig.appName}"
  )

// We can run this project by calling appJS/run and appJVM/run. Enter `projects` in SBT to see the name of all projects
lazy val app = crossProject.in(file("./app"))
  .settings(
    name := BuildConfig.appName,
    LogConfig.logDirKey := LogConfig.logDir,
    // The build info plugin writes these values into a BuildInfo object in the build info package
    buildInfoKeys := Seq(name,version,scalaVersion, sbtVersion, LogConfig.logDirKey)
  )
  .jvmSettings(
    libraryDependencies ++= Dependencies.server.value,
    javaOptions in Universal ++= RuntimeConfig.debugOptions((inDevMode)),
    javaOptions in Universal ++= RuntimeConfig.javaRuntimeOptions,

    // When running tests, we set credentials via this config instead of environment variables
    //javaOptions in Test += s"-Dconfig.file=${sourceDirectory.value}/test/resources/application.test.conf",
    // We need to fork a JVM process when testing so the Java options above are applied
    //fork in Test := true,
    //testExecutionExtraConfigTask := Map("config.file" -> s"${sourceDirectory.value}/test/resources/application.test.conf"),
    // Don't generate ScalaDoc when we are in development mode
    publishArtifact in(Compile, packageDoc) := !inDevMode,
    publishArtifact in packageDoc := !inDevMode,

    // Define the Docker image file for this application
    /*
      Prod mod : sbt -> project appJVM ->
        dockerComposeUp pull the last image from registry and compose with postgres
        dockerComposeUp skipPull compile and build a new image to compose locally
        dockerComposeRestart skipPull recompile and rebuild the image
        dockerBuildAndPush build the image WITHOUT compiling and push it to registry:latest
        DockerComposeStop stop and removes the memory.
      Dev mod : sbt -D"dev.mode=true" start in dev mode
    */
    imageNames in docker := {

      val gitLabRepository = s"registry.gitlab.com/vicou31/${BuildConfig.appName}"
      val defaultTag = if(inDevMode) "devlatest" else "prodlatest"
      // Tag the image with the value of the environment variable provided by GitLab continuous integration
      val imageTag = sys.env.getOrElse("CI_BUILD_REF", defaultTag)
      Seq(
        ImageName(
          repository = gitLabRepository,
          tag = Some(imageTag)
        )
      )
    },
    // The SBT Docker plugin uses this configuration to build a Docker image containing the app
    dockerfile in docker := {
      /*
      * The directory where JavaAppPackaging will put the directories for the binaries, dependencies,
      * and configuration of this app. When we call appJVM/docker, this path directory is "app/jvm/target/universal/stage"
       */
      val localAppDir = stage.value
      // The place inside the Docker container where we'll put the app's binaries
      val containerAppDir = "/app"
      new Dockerfile {
        // This app will run on Debian with a Java 8 JRE: https://hub.docker.com/_/openjdk/
        from("java:8-jre")

        copy(localAppDir, containerAppDir)

        // We define all port forwarding in docker-compose.yml

        // Inside the docker container, our application will put a log file into this directory.
        // In docker-compose.yml, we use a volume to keep the logs between container invocations
        run("mkdir", "-p", LogConfig.logDir)
       /* run("useradd","-m","myuser")
        user("myuser")
*/
        // JavaAppPackaging creates a shell script that starts the application
        private val startScript = executableScriptName.value
        //entryPoint(s"$containerAppDir/bin/$startScript")
        cmd(s"$containerAppDir/bin/$startScript")
      }
    },
    // The SBT Docker Compose plugin uses the following configuration
    // The base directory is "scala-js-example/app/jvm" and we want to read the file at "scala-js-example/docker-compose.yml"
    composeFile := s"${baseDirectory.value}/../../docker-compose.yml",

    // Create the image using the Docker plugin
    dockerImageCreationTask := docker.value

    /*
      We enable JavaAppPackaging to create a jar. Also, this gives us access to the variables stage and executableScriptName.
      Issuing "appJVM/docker" in SBT creates a Docker image from that jar.
     */
  )
  .enablePlugins(
    sbtdocker.DockerPlugin,
    DockerComposePlugin,
    JavaAppPackaging,
    BuildInfoPlugin
  )
  .jsSettings(
    libraryDependencies ++= Dependencies.client.scalaJsDependencies.value,
    jsDependencies ++= Dependencies.client.jsDependencies.value,
    dependencyOverrides += Dependencies.client.dependencyOverrides,
    // Include the JavaScript dependencies
    skip in packageJSDependencies := false,
    // Regardless of whether we optimize the created JavaScript fast or fully, produce a .js file with the same name.
    // This way, we don't have to adapt the name of the script to load at the client when we switch optimize modes
    artifactPath in Compile in fastOptJS := (crossTarget in fastOptJS).value / ((moduleName in fastOptJS).value + ".js"),
    artifactPath in Compile in fullOptJS := (crossTarget in fullOptJS).value / ((moduleName in fullOptJS).value + ".js"),
    // SBT adds the JSApp of our application as the main method in the produced JavaScript
    scalaJSUseMainModuleInitializer := true
  )

/*
  We need to define the subprojects. Note that the names of these vals do not affect how you run the subprojects:
  It will be `<nameOfCrossProject>JS/run` and `<nameOfCrossProject>JVM/run`, irrespective of how these vals are named
 */

lazy val client = app.js.settings()

lazy val server = app.jvm.settings(
  addJavaScriptToServerResources(),
  addJSDependenciesToServerResources()
)

/*
  Adds the compiled JavaScript to the server's resources so the server can send the JavaScript to the client
  @return a sequence of files that consists of our generated JavaScript file. Wrapped in a setting task for SBT
 */

def addJavaScriptToServerResources() = {
  if (inDevMode) {
    println(s"SBT for ${BuildConfig.appName} app is in dev mode")
    (resources in Compile) += (fastOptJS in(client, Compile)).value.data
  } else {
    println(s"SBT for ${BuildConfig.appName} app is in production mode")
    (resources in Compile) += (fullOptJS in(client, Compile)).value.data
  }
}

def addJSDependenciesToServerResources() = {
  (resources in Compile) += (packageMinifiedJSDependencies in(client, Compile)).value
}
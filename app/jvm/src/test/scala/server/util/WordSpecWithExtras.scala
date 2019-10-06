package server.util

import org.scalatest.concurrent.{Eventually, IntegrationPatience}
import org.scalatest.{ Matchers, fixture}
import slogging.{LazyLogging, LoggerConfig, SLF4JLoggerFactory}

/**
  * Bundles a couple of traits and abstract classes that allows extending classes to have asynchronous
  * WordSpecs with the ConfigMapFixture , logging, [[Matchers]] and the [[DockerSpecUtil]].
  * <p>
  * Created by Matthias Braun on 5/26/2017.
  */
trait WordSpecWithExtras extends fixture.AsyncWordSpec
  with fixture.AsyncConfigMapFixture
  with Matchers
  with Eventually
  with IntegrationPatience
  with LazyLogging
  with DockerSpecUtil {
  LoggerConfig.factory = SLF4JLoggerFactory()
}

package server.util

import org.scalatest.ConfigMap
import org.scalatest.exceptions.TestFailedException

/**
  * Helps with testing specifications that involve Docer containers.
  * <p>
  * Created by Matthias Braun on 5/26/2017.
  */
trait DockerSpecUtil {

  // The name and port (inside the container) of the Docker service as defined in docker-compose.yml
  private val webServiceName = "web"
  private val webServiceHostKey = s"$webServiceName:8080"

  private def containerSetting(configMap: ConfigMap, key: String): String =
    if (configMap.keySet.contains(key))
      configMap(key).toString
    else
      throw new TestFailedException(s"Cannot find the expected Docker Compose service key '$key' in the configMap. The configMap: '$configMap", 10)

  def hostname(configMap: ConfigMap): String = containerSetting(configMap, webServiceHostKey)

  def hostAddress (configMap: ConfigMap): String = s"http://${hostname(configMap)}"
}

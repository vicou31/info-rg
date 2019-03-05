package server.config

/*import java.io.FileNotFoundException
import java.security.{KeyStore, SecureRandom}
import javax.net.ssl.{KeyManagerFactory, SSLContext, TrustManagerFactory}
import akka.http.scaladsl.{ConnectionContext, HttpsConnectionContext}
import slogging.LazyLogging*/
import com.typesafe.config._
/*
import scala.util.{Failure, Try}*/

/**
  * Contains configuration for our server.
  * <p>
  * Created by Matthias Braun on 6/1/2017.
  */
object ServerConfig {

  /**
    * The server listens to incoming, unencrypted connections on this TCP port.
    * This port must be open using a port mapping in docker-compose.yml.
    */

    val config = ConfigFactory.load()
    val akkaHttpConfig = config.getConfig("akka.http")
    val port = akkaHttpConfig.getInt("server.port")


  /**
    * The server listens for connections on this IP address.
    * "0.0.0.0" means listening on all of the machine's IPv4 addresses. See also: https://en.wikipedia.org/wiki/0.0.0.0
    * <p>
    * "localhost" and "127.0.0.1" won't work here but connecting to the site from a browser using "localhost" works
    * fine if the server runs locally.
    */
  val interface = "0.0.0.0"

  /**
    * Contains configuration for transport layer security so server and client can use HTTPS.
    */
 /* object TLS extends LazyLogging {
    // Map this port of the container to 443 of the host in docker-compose.yml to enable HTTPS connections
    val port = 9090

    private def getKeyStore(password: Array[Char]): Try[KeyStore] = {
      val keyStoreFile = "tls/localhost.jks"
      val keystoreInputStream = getClass.getClassLoader.getResourceAsStream(keyStoreFile)

      if (keystoreInputStream == null)
        Failure(new FileNotFoundException(s"Can't find key store file at $keyStoreFile"))
      else
        Try {
          val keyStoreType = "JKS"
          val keyStore = KeyStore.getInstance(keyStoreType)
          keyStore.load(keystoreInputStream, password)
          keyStore
        }
    }

    private def getSslContext(password: Array[Char], keystore: KeyStore): Try[SSLContext] = {
      val keyManagerAlgorithm = "SunX509"

      Try {
        val sslContext = SSLContext.getInstance("TLS")
        val keyManagerFactory = KeyManagerFactory.getInstance(keyManagerAlgorithm)
        keyManagerFactory.init(keystore, password)
        val tmf = TrustManagerFactory.getInstance(keyManagerAlgorithm)
        tmf.init(keystore)
        sslContext.init(keyManagerFactory.getKeyManagers, tmf.getTrustManagers, new SecureRandom)

        sslContext
      }
    }

    private def getTlsKeyStorePassword: Array[Char] = {
      // We set this variable in docker-compose.yml
      val environmentVariableWithPassword = "TLS_KEY_STORE_PASSWORD"

      sys.env.getOrElse(environmentVariableWithPassword, {
        logger.warn("{} is unset", environmentVariableWithPassword)
        "unset"
      }).toCharArray
    }

    def connectionContext: Try[HttpsConnectionContext] = {
      val password = getTlsKeyStorePassword

      getKeyStore(password)
        .flatMap(keyStore => getSslContext(password, keyStore))
        .map(sslContext => ConnectionContext.https(sslContext)
        )
    }
  }*/

}


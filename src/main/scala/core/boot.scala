package core

import java.util.concurrent.TimeUnit
import akka.actor.{ Props, ActorSystem }
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import com.typesafe.config.{ ConfigFactory, Config }
import spray.can.Http
import scala.concurrent.Await

/**
 * Main application launcher.
 * - defines actor system for our application
 * - creates server instance
 * - add shutdown hook for actor system
 */
object Boot {

  implicit val system = ActorSystem("spray-slic-seed")

  def main(args: Array[String]): Unit = {

    class ApplicationServer(val actorSystem: ActorSystem) extends BootSystem with Api with ServerIO
    new ApplicationServer(system)

    sys.addShutdownHook(system.shutdown())
  }
}

/**
 * Binds http server to given host and port
 */
trait ServerIO {
  this: Api with BootSystem =>
  val config = ConfigFactory.load()

  IO(Http) ! Http.Bind(routeService, config.getString("application.server.host"), config.getInt("application.server.port"))
}

/**
 * Main boot system:
 * - configures database
 * - starts top-level actor (this actor will start sub-actors that will handle Api calls)
 */
trait BootSystem {
  final val startupTimeout = 15

  implicit def actorSystem: ActorSystem
  implicit val timeout: Timeout = Timeout(startupTimeout, TimeUnit.SECONDS)

  /**
   * Initialize database, propagate schema
   */
  DatabaseCfg.init()

  val application = actorSystem.actorOf(Props[ApplicationActor], "application")
  Await.ready(application ? Startup(), timeout.duration)

  actorSystem.registerOnTermination {
    application ! Shutdown()
  }
}
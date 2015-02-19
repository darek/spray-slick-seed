package core

import java.util.concurrent.TimeUnit

import akka.actor.{ ActorSystem, Props }
import akka.util.Timeout
import api.{ ErrorResponseException, Marshalling, TodoApi }
import spray.http.StatusCodes
import spray.routing._
import spray.util.LoggingContext

import scala.util.control.NonFatal

/**
 * Http Actor that handles URL calls
 */
class ApplicationApiActor(route: Route) extends HttpServiceActor with CustomErrorHandler {

  override def receive: Receive = runRoute(route)(customExceptionHandler, RejectionHandler.Default, actorRefFactory,
    RoutingSettings.default(actorRefFactory), LoggingContext.fromActorContext(actorRefFactory))

}

/**
 * This trait is used to join all APIS that we have
 * to add new api (eg. UserApi) You need to add it to routes
 *   val routes =
 *   new TodoApi().route ~
 *   new UserApi().route
 *
 * see `~` sign
 */
trait Api extends RouteConcatenation {
  this: BootSystem =>

  val routes =
    new TodoApi().route

  val routeService = actorSystem.actorOf(Props(new ApplicationApiActor(routes)))
}

/**
 * Use to configure when application should throw Timeout exception
 */
trait DefaultTimeout {
  implicit val timeout = new Timeout(2, TimeUnit.SECONDS)
}

/**
 * Custom error handler, If Api call can return OK or One error You can use eitherCustomErrorMarshaller to configure
 * what type of StatusCode should be returned
 */
trait CustomErrorHandler extends Marshalling {

  implicit def customExceptionHandler(implicit log: LoggingContext): ExceptionHandler =
    ExceptionHandler.apply {
      case NonFatal(ErrorResponseException(statusCode, entity)) =>
        log.error(s"Application return expected error status code ${statusCode} with entity ${entity} ")
        ctx => ctx.complete((statusCode, entity))
        case NonFatal(e) =>
        log.error(s"Application return unexpected error with exception ${e}")
        ctx => ctx.complete(StatusCodes.InternalServerError)
    }
}
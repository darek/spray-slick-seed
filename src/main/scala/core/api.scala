package core

import java.util.concurrent.TimeUnit

import akka.actor.Props
import akka.util.Timeout
import api.TodoApi
import spray.routing.{ Route, HttpServiceActor, RouteConcatenation }

/**
 * Created by darek on 17.02.15.
 */

class ApplicationApiActor(route: Route) extends HttpServiceActor {

  override def receive: Receive = runRoute(route)

}

trait Api extends RouteConcatenation {
  this: BootSystem =>

  val routes =
    new TodoApi().route

  val routeService = actorSystem.actorOf(Props(new ApplicationApiActor(routes)))
}

trait DefaultTimeout {
  implicit val timeout = new Timeout(1, TimeUnit.SECONDS)
}
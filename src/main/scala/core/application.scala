package core

import akka.actor.{ Props, Actor }
import service.TodoActor

/**
 * This actor:
 * - when receive Startup message it creates actors that will handle our requests
 * - when receive Shutdown message it stops all actors from context
 */
case class Startup()
case class Shutdown()

class ApplicationActor extends Actor {

  def receive: Receive = {
    case Startup() => {
      context.actorOf(Props[TodoActor], "todo")
      sender ! true
    }
    case Shutdown() => {
      context.children.foreach(ar => context.stop(ar))
    }
  }
}

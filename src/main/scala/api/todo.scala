package api

import akka.actor.ActorSystem
import akka.pattern.ask
import core.DefaultTimeout
import domain.{ Item, Todo }
import service.{ TodoFormats, CreateTodoList }
import spray.http.StatusCodes
import spray.routing.Directives

/**
 * Created by darek on 17.02.15.
 */
class TodoApi(implicit val actorSystem: ActorSystem) extends Directives with DefaultTimeout with TodoFormats {

  import scala.concurrent.ExecutionContext.Implicits.global

  val todoActor = actorSystem.actorSelection("/user/application/todo")

  val route =
    path("todo") {
      post {
        respondWithStatus(StatusCodes.Created) {
          handleWith { todo: Todo =>
            println(s"TODO $todo")
            (todoActor ? CreateTodoList(todo)).mapTo[Todo]
          }
        }
      }
    } /* ~
    path( "todo" / IntNumber ) { listId =>
        get {

        } ~
        post {

        } ~
        put {

        } ~
        delete {

        }
    } */

}
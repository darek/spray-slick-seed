package api

import akka.actor.ActorSystem
import akka.pattern.ask
import core.DefaultTimeout
import domain.{ Item, Todo }
import service._
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
            (todoActor ? CreateTodoList(todo)).mapTo[Todo]
          }
        }
      }
    } ~
      path("todo" / IntNumber) { listId =>
        get {
          complete {
            (todoActor ? GetItemsList(listId)).mapTo[Either[UnknownTodoList, List[Item]]]
          }
        } ~
          post {
            respondWithStatus(StatusCodes.Created) {
              handleWith { item: Item =>
                (todoActor ? InsertItem(item.copy(list = Some(listId)))).mapTo[Item]
              }
            }
          }
      }

}
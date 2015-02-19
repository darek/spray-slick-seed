package api

import akka.actor.ActorSystem
import akka.pattern.ask
import core.DefaultTimeout
import domain.{ Item, Todo }
import service._
import spray.http.{ ContentTypes, HttpEntity, HttpResponse, StatusCodes }
import spray.routing.Directives

/**
 * Sample API for TodoList
 * Each api path is in separate spray 'path' directive, in my opinion is cleaner that nesting
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
            (todoActor ? GetItemsList(listId)).mapTo[Either[TodoListOperationError, List[Item]]]
          }
        } ~
          post {
            respondWithStatus(StatusCodes.Created) {
              handleWith { item: Item =>
                (todoActor ? InsertItem(item.copy(list = Some(listId)))).mapTo[Item]
              }
            }
          }
      } ~
      path("todo" / IntNumber / IntNumber) { (listId, itemId) =>
        delete {
          complete {
            (todoActor ? DeleteItem(listId, itemId)).mapTo[Either[TodoListOperationError, Int]]
              .map[HttpResponse] {
                case Left(error) => error match { // if call returns error, then we match error type to response code
                  case lde: UnknownTodoList =>
                    HttpResponse(status = StatusCodes.NotFound)
                  case ups: OperationNotSupported => HttpResponse(status = StatusCodes.NotImplemented,
                    entity = HttpEntity(ContentTypes.`application/json`, TodoListOperationErrorFormat.write(ups).toString))
                }
                case Right(dNum) => HttpResponse(status = StatusCodes.NoContent)
              }
          }
        }
      }

}
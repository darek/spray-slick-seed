package service

import akka.actor.Actor
import api.Marshalling
import domain.{ Todo, Item }
import spray.http.StatusCodes
import spray.json.ProductFormats
import core.DatabaseCfg._
import scala.slick.driver.H2Driver.simple._

/**
 * Case classes for Akka messaging
 */

case class CreateTodoList(todo: Todo)
case class GetItemsList(listId: Int)
case class InsertItem(item: Item)
case class DeleteItem(listId: Int, itemId: Int)

/**
 * Akka actor that will handle API calls
 */
class TodoActor extends Actor with TodoActions {
  def receive: Receive = {
    case CreateTodoList(todo) =>
      sender ! createTodoList(todo)

    case GetItemsList(listId) =>
      sender ! getItemsList(listId)

    case InsertItem(item) =>
      sender ! insertItem(item)

    case DeleteItem(listId, itemId) =>
      sender ! deleteItem(listId, itemId)
  }
}

/**
 * Trait with bussiness logic for TodoActor
 */
trait TodoActions {

  def createTodoList(todo: Todo): Todo = {
    db.withSession { implicit session =>
      val todoId = (todosTable returning todosTable.map(_.id)) += todo
      todo.copy(id = Some(todoId.toInt))
    }
  }

  def getTodo(todoId: Int): Option[Todo] = {
    db.withSession { implicit session =>
      todosTable.filter(t => t.id === todoId).run.headOption
    }
  }

  def getItemsList(listId: Int): Either[TodoListOperationError, List[Item]] = {
    db.withSession { implicit session =>
      getTodo(listId) match {
        case Some(list) => Right(itemsTable.filter(item => item.list === listId).list)
        case None => Left(UnknownTodoList())
      }
    }
  }

  def insertItem(item: Item): Item = {
    db.withSession { implicit session =>
      val itemId = (itemsTable returning itemsTable.map(_.id)) += item
      item.copy(id = Some(itemId.toInt))
    }
  }

  /**
   * This action is not Yet implemented, You can try to implement it
   */
  def deleteItem(todoListId: Int, itemId: Int): Either[TodoListOperationError, Int] = {
    Left(OperationNotSupported("delete"))
  }
}

/**
 * Error case classes
 */
trait TodoListOperationError
case class UnknownTodoList() extends TodoListOperationError
case class OperationNotSupported(opName: String) extends TodoListOperationError

/**
 * Trait with defined implicits for marshalling
 * Should be in TodoApi
 */
trait TodoFormats extends Marshalling with ProductFormats {
  import spray.json._

  implicit val TodoFormat = jsonFormat2(Todo)
  implicit val ItemFormat = jsonFormat3(Item)
  implicit val TodoListOperationErrorEitherFormat = eitherCustomMarshaller[TodoListOperationError, List[Item]](StatusCodes.NotFound)

  implicit object TodoListOperationErrorFormat extends RootJsonFormat[TodoListOperationError] {
    override def read(json: JsValue): TodoListOperationError = sys.error("Only write is available for failures")

    override def write(obj: TodoListOperationError): JsValue = obj match {
      case UnknownTodoList() => JsString("List does not exists")
      case OperationNotSupported(opName: String) => JsString(s"Operation '$opName' is not yet supported.")
      case _ => JsString("Operation failed bro.")
    }
  }

}
package service

import akka.actor.Actor
import api.Marshalling
import domain.{ Items, Todo, Item }
import spray.http.StatusCodes
import spray.json.ProductFormats
import core.DatabaseCfg._
import scala.slick.driver.H2Driver.simple._

/**
 * Created by darek on 17.02.15.
 */

case class CreateTodoList(todo: Todo)
case class GetItemsList(listId: Int)
case class InsertItem(item: Item)

class TodoActor extends Actor with TodoActions {
  def receive: Receive = {
    case CreateTodoList(todo) =>
      sender ! createTodoList(todo)

    case GetItemsList(listId) =>
      sender ! getItemsList(listId)

    case InsertItem(item) =>
      sender ! insertItem(item)
  }
}

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

  def getItemsList(listId: Int): Either[UnknownTodoList, List[Item]] = {
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
}

case class UnknownTodoList()

trait TodoFormats extends Marshalling with ProductFormats {

  implicit val TodoFormat = jsonFormat2(Todo)
  implicit val ItemFormat = jsonFormat3(Item)
  implicit val UnknownTodoListFormat = jsonFormat0(UnknownTodoList)

  implicit val UnknownTodoListEitherFormat = eitherCustomMarshaller[UnknownTodoList, List[Item]](StatusCodes.NotFound)

}
package service

import akka.actor.Actor
import api.Marshalling
import domain.{ Todo, Item }
import spray.json.ProductFormats
import core.DatabaseCfg._
import scala.slick.driver.H2Driver.simple._

/**
 * Created by darek on 17.02.15.
 */

case class CreateTodoList(todo: Todo)

class TodoActor extends Actor with TodoActions {
  def receive: Receive = {
    case CreateTodoList(todo) =>
      sender ! createTodoList(todo)
  }
}

trait TodoActions {

  def createTodoList(todo: Todo): Todo = {
    println("Inserting todo")
    db.withSession { implicit session =>
      val todoId = (todosTable returning todosTable.map(_.id)) += todo
      todo.copy(id = Some(todoId.toInt))
    }
  }
}

trait TodoFormats extends Marshalling with ProductFormats {

  implicit val TodoFormat = jsonFormat2(Todo)
  implicit val ItemFormat = jsonFormat3(Item)

}
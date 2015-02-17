package domain

import scala.slick.driver.H2Driver.simple._

/**
 * Created by darek on 17.02.15.
 */

case class Todo(id: Option[Int], name: String)

class Todos(tag: Tag) extends Table[Todo](tag, "Todo") {

  def id: Column[Int] = column[Int]("id", O.NotNull, O.AutoInc, O.PrimaryKey)
  def name: Column[String] = column[String]("name", O.NotNull)
  def * = (id.?, name) <> (Todo.tupled, Todo.unapply _)
}

package domain

import scala.slick.driver.H2Driver.simple._

/**
 * Todo case class, stores information about todo lists
 */

case class Todo(id: Option[Int], name: String)

/**
 * Slick Todo table definition
 */
class Todos(tag: Tag) extends Table[Todo](tag, "Todo") {

  def id: Column[Int] = column[Int]("id", O.NotNull, O.AutoInc, O.PrimaryKey)
  def name: Column[String] = column[String]("name", O.NotNull)
  def * = (id.?, name) <> (Todo.tupled, Todo.unapply _)
}

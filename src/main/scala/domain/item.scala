package domain

import scala.slick.driver.H2Driver.simple._
import core.DatabaseCfg._
/**
 * Item case class, stores information about item
 */
case class Item(id: Option[Int], list: Option[Int], label: String)

/**
 * Slick Item table definition
 */
class Items(tag: Tag) extends Table[Item](tag, "Item") {
  def id: Column[Int] = column[Int]("id", O.AutoInc, O.NotNull, O.PrimaryKey)
  def list: Column[Int] = column[Int]("list", O.NotNull)
  def label: Column[String] = column[String]("label", O.NotNull)
  def * = (id.?, list.?, label) <> (Item.tupled, Item.unapply _)
  def todoList = foreignKey("item_list", list, todosTable)(_.id, onDelete = ForeignKeyAction.Cascade)
}

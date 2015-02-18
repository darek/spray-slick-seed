package domain

import scala.slick.driver.H2Driver.simple._

/**
 * Created by darek on 17.02.15.
 */
case class Item(id: Option[Int], list: Option[Int], label: String)

class Items(tag: Tag) extends Table[Item](tag, "Item") {
  def id: Column[Int] = column[Int]("id", O.AutoInc, O.NotNull, O.PrimaryKey)
  def list: Column[Int] = column[Int]("list", O.NotNull)
  def label: Column[String] = column[String]("label", O.NotNull)
  def * = (id.?, list.?, label) <> (Item.tupled, Item.unapply _)
}

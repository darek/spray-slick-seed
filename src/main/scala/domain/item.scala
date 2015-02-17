package domain

import scala.slick.driver.H2Driver.simple._

/**
 * Created by darek on 17.02.15.
 */
case class Item(id: Option[Int], list: Int, label: String)

class Items(tag: Tag) extends Table[Item](tag, "Item") {
  def id: Column[Int] = column[Int]("id")
  def list: Column[Int] = column[Int]("list")
  def label: Column[String] = column[String]("label")
  def * = (id.?, list, label) <> (Item.tupled, Item.unapply _)
}

package core

import domain.{ Items, Todos }
import scala.slick.driver.H2Driver.simple._
import scala.slick.jdbc.meta.MTable

/**
 * Database configuration
 */
object DatabaseCfg {

  // For H2 in memory database we need to use DB_CLOSE_DELAY
  val db = Database.forURL("jdbc:h2:mem:todo-list;DATABASE_TO_UPPER=false;DB_CLOSE_DELAY=-1");

  // create TableQueries for all tables
  val todosTable: TableQuery[Todos] = TableQuery[Todos]
  val itemsTable: TableQuery[Items] = TableQuery[Items]

  // Initialize database if tables does not exists
  def init() = {
    db.withTransaction { implicit session =>
      if (MTable.getTables("Todo").list.isEmpty) {
        todosTable.ddl.create
      }
      if (MTable.getTables("Item").list.isEmpty) {
        itemsTable.ddl.create
      }
    }
  }
}

package core

import domain.{ Items, Todos }
import scala.slick.driver.H2Driver.simple._
import scala.slick.jdbc.meta.MTable

/**
 * Created by darek on 17.02.15.
 */
object DatabaseCfg {

  val db = Database.forURL("jdbc:h2:mem:todo-list;DATABASE_TO_UPPER=false;DB_CLOSE_DELAY=-1"); //forDataSource(pooledDataSource)

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

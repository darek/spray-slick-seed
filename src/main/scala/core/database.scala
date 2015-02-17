package core

import com.typesafe.config.ConfigFactory
import com.mchange.v2.c3p0.ComboPooledDataSource
import domain.{ Items, Todos }
import scala.slick.driver.H2Driver
import scala.slick.driver.H2Driver.simple._
/**
 * Created by darek on 17.02.15.
 */
object DatabaseCfg {

  //val pooledDataSource = new ComboPooledDataSource
  val db = Database.forURL("jdbc:h2:mem:todo-list"); //forDataSource(pooledDataSource)

  // create TableQueries for all tables
  val todosTable: TableQuery[Todos] = TableQuery[Todos]
  val itemsTable: TableQuery[Items] = TableQuery[Items]

  def init() = {
    db.withSession { implicit session =>
      println("Creating schema")
      (todosTable.ddl ++ itemsTable.ddl).create
    }
  }
}

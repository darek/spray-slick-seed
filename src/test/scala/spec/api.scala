package spec

import java.util.concurrent.TimeUnit

import core.{ CustomErrorHandler, BootSystem, Api }
import org.specs2.mutable.{ Before, Specification }
import spray.routing.HttpService
import spray.testkit.Specs2RouteTest
import core.DatabaseCfg._
import scala.slick.driver.H2Driver.simple._
import scala.concurrent.duration.FiniteDuration

class TestSpec extends ApiSpec with Before with CustomErrorHandler {

  /**
   * We have custom exception handler defined (for EitherMarshaller) so we need to
   * inform our test env that we are using it.
   */
  val customException = handleExceptions(customExceptionHandler)

  /**
   * Remove all records before tests
   */
  def before(): Unit = {
    db.withTransaction { implicit session =>
      itemsTable.delete
      todosTable.delete
    }
  }

}

/**
 * Hooking up to our API
 */
class ApiSpec extends Specification with Specs2RouteTest with HttpService with Api with BootSystem {
  implicit val routeTestTimeout: RouteTestTimeout = RouteTestTimeout(FiniteDuration(5, TimeUnit.SECONDS))
  implicit def actorSystem = system
  def actorRefFactory = system

}

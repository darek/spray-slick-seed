package spec

import java.util.concurrent.TimeUnit

import core.{ BootSystem, Api }
import org.specs2.mutable.Specification
import spray.routing.HttpService
import spray.testkit.Specs2RouteTest

import scala.concurrent.duration.FiniteDuration

/**
 * Created by darek on 18.02.15.
 */
class ApiSpec extends Specification with Specs2RouteTest with HttpService with Api with BootSystem {
  implicit val routeTestTimeout: RouteTestTimeout = RouteTestTimeout(FiniteDuration(5, TimeUnit.SECONDS))
  implicit def actorSystem = system
  def actorRefFactory = system

}

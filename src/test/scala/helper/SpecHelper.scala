package helper

import org.slf4j.LoggerFactory
import spec.ApiSpec
import spray.httpx.marshalling.{ Marshaller, ToResponseMarshaller }
import spray.httpx.unmarshalling.FromResponseUnmarshaller
import scala.util.Random

import scala.reflect.ClassTag

/**
 * Created by darek on 18.02.15.
 */
class SpecHelper(val prefix: String) extends ApiSpec {

  args(skipAll = true)

  val logger = LoggerFactory.getLogger(getClass)

  def _Post[A, B: ToResponseMarshaller: ClassTag](path: String, entity: A)(implicit ma: Marshaller[A], mb: FromResponseUnmarshaller[B]): B = {
    logger.debug(s"Sending POST($path) request with entity=$entity")
    Post(path, entity) ~> routes ~> check {
      responseAs[B]
    }
  }

  def _Get[A: ToResponseMarshaller: ClassTag](path: String)(implicit ma: FromResponseUnmarshaller[A]): A = {
    logger.debug(s"Sending GET($path) request")
    Get(path) ~> routes ~> check {
      responseAs[A]
    }
  }

  def _Put[A, B: ToResponseMarshaller: ClassTag](path: String, entity: A)(implicit ma: Marshaller[A], mb: FromResponseUnmarshaller[B]): B = {
    logger.debug(s"Sending POST($path) request with entity=$entity")
    Put(path, entity) ~> routes ~> check {
      responseAs[B]
    }
  }

  def _Delete[A, B: ToResponseMarshaller: ClassTag](path: String, entity: A)(implicit ma: Marshaller[A], mb: FromResponseUnmarshaller[B]): B = {
    logger.debug(s"Sending POST($path) request with entity=$entity")
    Delete(path, entity) ~> routes ~> check {
      responseAs[B]
    }
  }

  def randomString(num: Int): String = Random.alphanumeric.take(num).mkString
}

package helper

import org.slf4j.LoggerFactory
import spec.ApiSpec
import spray.httpx.marshalling.{ Marshaller, ToResponseMarshaller }
import spray.httpx.unmarshalling.FromResponseUnmarshaller
import scala.util.Random

import scala.reflect.ClassTag

/**
 * Spec helper, should be extended by helpers for specific Apis.
 * Use it to populate test data (eg. create TodoList when we test saving items to list)
 */
class SpecHelper(val prefix: String) extends ApiSpec {

  args(skipAll = true)

  val logger = LoggerFactory.getLogger(getClass)

  /**
   * Function that sends Post request and returns result
   * @param path where we should send request (eg. /todo/1)
   * @param entity what we should send
   * @param ma marshaller for request (this should be automaticaly linked with implicit from *Format trait)
   * @param mb marshaller for response (this should be automaticaly linked with implicit from *Format trait)
   * @tparam A type of object we are sending
   * @tparam B type of object we need from response
   * @return
   */
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

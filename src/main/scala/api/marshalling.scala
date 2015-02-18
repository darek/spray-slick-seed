package api

import spray.http.{ HttpEntity, StatusCode }
import spray.httpx.SprayJsonSupport
import spray.httpx.marshalling.{ MetaMarshallers, CollectingMarshallingContext, ToResponseMarshaller, Marshaller }
import spray.json.DefaultJsonProtocol

/**
 * Case class that represents Error inside application
 * @param code Status code that will be returned in response
 * @param entity Response entity
 */
case class ErrorResponseException(code: StatusCode, entity: HttpEntity) extends Throwable

/**
 * Main trait for marshalling support
 */
trait Marshalling extends DefaultJsonProtocol with SprayJsonSupport with MetaMarshallers {

  /**
   * Function for handling errors when API returns Left(ERROR) or Right(Response)
   * For more information how eitherCustomMarshaller works check Jan Machacek entry about errors and failures:
   * http://www.cakesolutions.net/teamblogs/2012/12/10/errors-failures-im-a-teapot
   */
  implicit def eitherCustomMarshaller[A, B](code: StatusCode)(implicit ma: Marshaller[A], mb: Marshaller[B]): ToResponseMarshaller[Either[A, B]] =
    Marshaller[Either[A, B]] { (value, ctx) =>
      value match {
        case Left(a) =>
          val mc = new CollectingMarshallingContext()
          ma(a, mc)
          ctx.handleError(ErrorResponseException(code, mc.entity))
        case Right(b) =>
          (200, mb(b, ctx))
      }
    }

}
package api

import spray.httpx.SprayJsonSupport
import spray.json.DefaultJsonProtocol

/**
 * Created by darek on 17.02.15.
 */
trait Marshalling extends DefaultJsonProtocol with SprayJsonSupport
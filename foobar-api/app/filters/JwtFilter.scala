package filters

import javax.inject._
import akka.stream.Materializer
import play.api.Logging
import play.api.mvc._
import play.api.libs.json._
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import authentikat.jwt._

@Singleton
class JwtFilter @Inject()(implicit val mat: Materializer, ec: ExecutionContext) extends Filter with Logging {

    def apply(next: RequestHeader => Future[Result])(requestHeader: RequestHeader): Future[Result] = {
        val token = requestHeader.headers.get("Authorization")

        if(token.isEmpty) {
            return Future.successful(Results.Unauthorized("No token available"))
        }

        // Bearer adsöfdsö435e0ffogsdlöm
        val splitAuthToken = token.get.split(" ")

        val jwtToken = splitAuthToken(1)

        logger.info(jwtToken)

        val claimsJsonString: Option[String] = jwtToken match {
            case JsonWebToken(header, claimsSet, signature) =>
                logger.info("Valid JWT found")
                Option(claimsSet.asJsonString)
            case x =>
                logger.info("No valid JWT found")
                None
        }

        val claims = Json.parse(claimsJsonString.get)

        val audience = (claims \ "aud").get.toString
        val iss = (claims \ "iss").get.toString
        val sub = (claims \ "sub").get.toString
        val roles = (claims \ "roles").get

        logger.info(iss)
        logger.info(sub)

        next(requestHeader).map { result =>
            result
                .withHeaders("Audience" -> audience)
                .withHeaders("iss" -> iss)
                .withHeaders("sub" -> sub)
        }
    }
}
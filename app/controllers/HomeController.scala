package controllers

import javax.inject._

import play.api._
import javax.inject.Inject

import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.mvc.Security
import play.api.mvc._
import services.UserService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import utils.{JwtSupport, User}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents, authAction: AuthAction, authTransformer: AuthReqTransformer,userService: UserService) extends AbstractController(cc) {
    def login = Action { request =>
      val jsonBody = request.body.asJson

      jsonBody match {
        case Some(body) => {
          val userId = (body \ "userId").as[String]
          val password = (body \ "password").as[String]

          if(userService.isExistingUser(User(userId, password))){
            Logger.info("Is a Valid User")
            val payload = Json.obj(
              "id" -> userId,
              "emailId" ->  "admin@ether.com",
              "expTime" -> 36000,
              "role" -> "admin"
            )

            val token = JwtSupport.createToken(Json.stringify(payload))
            Ok(Json.toJson(Map("token" -> token)))
//            Ok(token)
          }
          else{
            Logger.info("Unauthorized User")
            Ok(Results.Unauthorized.toString())
          }
        }
        case None => {
          Ok("Failure")
        }
      }
    }

    def signup = Action { request =>
      val jsonBody = request.body.asJson

      jsonBody match {
        case Some(body) => {
          val userId = (body \ "userId").as[String]
          val password = (body \ "password").as[String]

          val userTuple = User(userId, password)

          userService.createNewUser(userTuple)

          Ok("Success")
        }
        case None => {
          Ok("Failure")
        }
      }
    }

    def query = (authAction andThen authTransformer) {request =>

      request.user match {
        case s: UserClaims => {
          Logger.info(s"Request is ${request.user}")
          Ok(Json.toJson(Map("id" -> request.user.id, "emailId" -> request.user.emailId, "role" -> request.user.role)))
//          Ok(views.html.index())
        }
        case _ => {
          Logger.info(s"Claims are missing ")
          Ok("Error")
        }
      }
    }
}

case class UserClaims(id: String, emailId: String, role: String, expTime: Int)

class AuthAction @Inject()(parser: BodyParsers.Default, jwtUtility: JwtSupport)(implicit ec: ExecutionContext) extends ActionBuilderImpl(parser) {

  implicit val claimsReads: Reads[UserClaims] = (
    (JsPath \ "id").read[String] and
      (JsPath \ "emailId").read[String] and
      (JsPath \ "role").read[String] and
      (JsPath \ "expTime").read[Int]
    )(UserClaims.apply _)

  override def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]) = {
    Logger.info("Calling action")

    val AuthHeader = request.headers.get("Authorization")

    AuthHeader match {
      case Some(header) => {
        Logger.info(s"$header")

        //TODO: Handle this properly
        val jwtToken = header.split(" ")(1).trim

        Logger.info(s"JWT_TOKEN is $jwtToken")

        if(jwtUtility.isValidToken(jwtToken)){
          Logger.info("Is a valid token ")
          val claims = jwtUtility.decodePayload(jwtToken)

          claims match {
            case Some(c) => {
              //TODO: Use validate here rather than direct typecasting
              val claimsJson = Json.parse(c).validate[UserClaims]

              Logger.info(s"ClaimsJson is $claimsJson")

              claimsJson match {
                case s: JsSuccess[UserClaims] => {
                  Logger.info(s"ClaimsSet is $c")
                  block(new UserRequest[A](s.get, request))
                }
                case e: JsError => {
                  Logger.info(s"ClaimsSet structure is different ")
                  Future.successful(Results.Unauthorized)
                }
              }
            }
            case None => {
              Logger.info(s"ClaimsSet is empty ")
              Future.successful(Results.Unauthorized)
            }
          }
        }
        else{
          Logger.info("Is a invalid Token ")
          Future.successful(Results.Unauthorized)
        }
      }
      case None => {
        Logger.info("Header is missing ")
        Future.successful(Results.Redirect("/login"))
      }
    }
  }
}

class UserRequest[A](val user: UserClaims, request: Request[A]) extends WrappedRequest[A](request)

class AuthReqTransformer @Inject()(parser: BodyParsers.Default, jwtUtility: JwtSupport)(implicit ec: ExecutionContext) extends ActionBuilderImpl(parser) with ActionTransformer[Request, UserRequest] {
  override protected def transform[A](request: Request[A]): Future[UserRequest[A]] = Future.successful {
    new UserRequest[A](UserClaims("1234","admin@ether.com","admin",36000), request)
  }
}
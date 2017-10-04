package controllers

import authentikat.jwt.{JsonWebToken, JwtClaimsSet, JwtHeader}
import javax.inject.Inject

import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.mvc.Security
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by bala on 27/9/17.
  */
//class JwtUtility {
//  val JwtSecretKey = "secretKey"
//  val JwtSecretAlgo = "HS256"
//
//  def createToken(payload: String): String = {
//    val header = JwtHeader(JwtSecretAlgo)
//    val claimSet = JwtClaimsSet(payload)
//
//    JsonWebToken(header, claimSet, JwtSecretKey)
//  }
//
//  def isValidToken(jwtToken: String): Boolean = JsonWebToken.validate(jwtToken, JwtSecretKey)
//
//  def decodePayload(jwtToken: String): Option[String] = {
//    jwtToken match {
//      case JsonWebToken(header, claimsSet, signature) => Option(claimsSet.asJsonString)
//      case _  => None
//    }
//  }
//}
//
//object JwtUtility extends JwtUtility
//
//class DataSource {
//  def getUser(email: String, userId: String): Option[UserInfo] =
//    if (email == "test@example.com" && userId == "userId123") {
//      Some(UserInfo(1, "John", "Nash", email))
//    } else {
//      None
//    }
//}
//
//
//case class UserInfo(id: Int,
//                    firstName: String,
//                    lastName: String,
//                    email: String)

//case class User(email: String, userId: String)
//
//case class UserRequest[A](userInfo: UserInfo, request: Request[A]) extends WrappedRequest(request)
//
//class AuthAction @Inject()(par: BodyParsers.Default)(implicit ec: ExecutionContext)  extends ActionBuilder[UserRequest, AnyContent] {
//  implicit val placeReads: Reads[User] = (
//    (JsPath \ "email").read[String] and
//    (JsPath \ "userId").read[String]
//  )(User.apply _)
//
//
//  override def invokeBlock[A](request: Request[A], block: (UserRequest[A]) => Future[Result]): Future[Result] = {
//    val jwtToken = request.headers.get("jw_token").getOrElse("")
//
////    val jwtToken = JwtUtility.createToken("{ \"email\": \"test@example.com\", \"userId\" : \"userId123\"}")
//
//    if (JwtUtility.isValidToken(jwtToken)) {
//      JwtUtility.decodePayload(jwtToken).fold {
//        Future.successful(Results.Unauthorized("Invalid credential1"))
//      } { payload =>
//        val userCredentials = Json.parse(payload).validate[User].get
//
//        // Replace this block with data source
//        val dataSource = new DataSource()
//        val maybeUserInfo = dataSource.getUser(userCredentials.email, userCredentials.userId)
//
//        maybeUserInfo.fold(Future.successful(Results.Unauthorized("Invalid credential2")))(userInfo => block(UserRequest(userInfo, request)))
//      }
//    } else {
//      Future.successful(Results.Unauthorized("Invalid credential3"))
//    }
//  }
//
//  override def parser: BodyParser[AnyContent] = par
//
//  override protected def executionContext: ExecutionContext = ec
//}

/*
* def authorizeUsingPolicy(policy: AuthorizationPolicy): ActionRefiner[RequestWithPrincipal, RequestWithPrincipal] =
    new ActionRefiner[RequestWithPrincipal, RequestWithPrincipal] {
      override protected def refine[A](request: RequestWithPrincipal[A]): Future[Either[Result, RequestWithPrincipal[A]]] = {
        if(!policy.allowed(request)) {
          authorizationHandler.denied(request).map(Left(_))
        } else {
          Future.successful(Right(request))
        }
      }
    }*/


/*
* def ItemAction(itemId: String)(implicit ec: ExecutionContext) = new ActionRefiner[UserRequest, ItemRequest] {
  def executionContext = ec
  def refine[A](input: UserRequest[A]) = Future.successful {
    ItemDao.findById(itemId)
      .map(new ItemRequest(_, input))
      .toRight(NotFound)
  }
}*/


//class AuthenticRequest[A](val user: Option[UserInfo], request: Request[A]) extends WrappedRequest[A](request)

//class AuthSupport {
//  def Authenticate(implicit ec: ExecutionContext): ActionRefiner[Request, AuthenticRequest] = new ActionRefiner[Request, AuthenticRequest] {
//    override def executionContext = ec
//    override def refine[A](request: Request[A]): Future[Either[Result, AuthenticRequest[A]]] = {
//      val authHeader = request.headers.get("Authorization")
//      authHeader match {
//        case Some(header) => {
//          val r = Future.successful(Right(new AuthenticRequest(Some(UserInfo("bala","pwd")),request)))
//          r
//        }
//        case None => Future.successful(Left(Results.Unauthorized))
//      }
//
//    }
//  }
//}

//object AuthSupport extends AuthSupport
package utils

import authentikat.jwt.{JsonWebToken, JwtClaimsSet, JwtHeader}

/**
  * Created by bala on 3/10/17.
  */

trait TokenSupport {
  def createToken(payload: String): String
  def isValidToken(token: String): Boolean
  def decodePayload(token: String): Option[String]
}

class JwtSupport extends TokenSupport{
  val JwtSecretKey = "bingo"
  val JwtSecretAlgo = "HS256"

  override def createToken(payload: String): String = {
    val header = JwtHeader(JwtSecretAlgo)
    val claims = JwtClaimsSet(payload)

    JsonWebToken(header, claims, JwtSecretKey)
  }

  override def isValidToken(jwtToken: String): Boolean = JsonWebToken.validate(jwtToken, JwtSecretKey)

  override def decodePayload(jwtToken: String): Option[String] = {
    jwtToken match {
      case JsonWebToken(header, claimsSet, signature) => Option(claimsSet.asJsonString)
      case _  => None
    }
  }
}

object JwtSupport extends JwtSupport

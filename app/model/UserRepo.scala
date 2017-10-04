package model

/**
  * Created by bala on 3/10/17.
  */
import utils.User
import play.api._

class UserRepo {
  def createNewUser(user: User) = {
    Logger.info("New User Successfully Created")
  }

  def isExistingUser(user: User) = {
    user.userId == "USER" && user.password == "pwd"
  }
}

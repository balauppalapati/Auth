package services

import play.api.Logger
import utils.User
import model.UserRepo

import javax.inject._

/**
  * Created by bala on 3/10/17.
  */

class UserService @Inject()(userRepo: UserRepo){
  def createNewUser(user: User) = userRepo.createNewUser(user)
  def isExistingUser(user: User) = userRepo.isExistingUser(user)
}

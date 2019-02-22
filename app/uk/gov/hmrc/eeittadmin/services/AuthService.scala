/*
 * Copyright 2019 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.eeittadmin.services

import cats.data.Validated
import play.api.Logger
import play.api.libs.json.{Json, OFormat}
import uk.gov.hmrc.eeittadmin.models.{Email, QueryPermission, User}
import uk.gov.hmrc.eeittadmin.repositories.UserRespository

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class AuthService(userRepository: UserRespository) {

  def checkPermissions(email: Email): Future[Validated[LoginError, User]] = {
    userRepository.getUser(email).map {
      case Nil =>
        Logger.error("No User")
        Validated.invalid(LoginError(List("SomeError")))
      case head :: _ =>
        Logger.info("A user exists for the id ")
        Validated.valid(head)
    }
  }

  def register(user : User): Future[Validated[RegisterError, String]] = {
    userRepository.registerUser(user)
  }
}

case class LoginError(error: List[String])

object LoginError {

  implicit val format: OFormat[LoginError] = Json.format[LoginError]
}

case class RegisterError(error: List[String])

object RegisterError {

  implicit val format: OFormat[RegisterError] = Json.format[RegisterError]
}

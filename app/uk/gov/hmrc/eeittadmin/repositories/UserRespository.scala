/*
 * Copyright 2017 HM Revenue & Customs
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

package uk.gov.hmrc.eeittadmin.repositories

import cats.data.Validated
import play.api.libs.json.Json
import reactivemongo.api.DB
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.BSONObjectID
import uk.gov.hmrc.eeittadmin.models.{Email, User}
import uk.gov.hmrc.eeittadmin.services.RegisterError
import uk.gov.hmrc.mongo.ReactiveRepository

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait UserRespo {

  def getUser(email: Email) : Future[List[User]]
  def registerUser(user: User) : Future[Validated[RegisterError, String]]
}

class UserRespository(implicit mongo : () => DB)
  extends ReactiveRepository[User, BSONObjectID]("eeittadminusers", mongo, Json.format[User]) with UserRespo {

  override def getUser(email: Email): Future[List[User]] = {
    find("email.value" -> email.value)
  }

  override def registerUser(user: User): Future[Validated[RegisterError, String]] = {
    insert(user).map{ result =>
      if(result.ok){
        Validated.valid("success")
      } else {
        Validated.invalid(RegisterError(List("Registration Failed")))
      }
    }
  }




}

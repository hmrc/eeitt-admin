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

package uk.gov.hmrc.eeittadmin.controllers

import cats.data.Validated.{Invalid, Valid}
import play.api.Logger
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc.Action
import uk.gov.hmrc.eeittadmin.models.{Email, User}
import play.api.mvc._
import uk.gov.hmrc.eeittadmin.services.{AuthService, LoginError, RegisterError}
import uk.gov.hmrc.play.microservice.controller.BaseController

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

class AuthController(authService: AuthService) extends BaseController with LegacyI18nSupport {

  def authenticate: Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[Email] match {
      case JsSuccess(req, _) =>
        authService.checkPermissions(req).map {
          case Valid(x) =>
            Logger.debug(Json.prettyPrint(Json.toJson(x)))
            Ok(Json.toJson(x))
          case Invalid(err) =>
            Logger.debug(err.error.toString())
            Ok(Json.toJson(err))
        }
      case JsError(err) =>
        Logger.debug(err.toString)
        val errors = err.flatMap(x => x._2.map(_.message)).toList
        Future.successful(Ok(Json.toJson(request.body)))//LoginError(errors))))
    }
  }

  def register: Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[User] match {
      case JsSuccess(x, _) =>
        Logger.debug(x.toString)
        authService.register(x).map {
          case Valid(str) => Ok(Json.toJson(str))
          case Invalid(err) => Ok(Json.toJson(err))
        }
      case JsError(err) =>
        Logger.debug(err.toString)
        Future.successful(Ok(Json.toJson(err.toString)))
    }
  }
}

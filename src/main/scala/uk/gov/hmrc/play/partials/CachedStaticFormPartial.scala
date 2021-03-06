/*
 * Copyright 2015 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.play.partials

import play.api.mvc.RequestHeader
import play.twirl.api.Html

trait CachedStaticFormPartial extends CachedStaticHtmlPartial {

  override def processTemplate(template: Html, parameters: Map[String, String])(implicit request: RequestHeader): Html = {

    val formParameters = parameters + ("csrfToken" -> getCsrfToken)
    super.processTemplate(template, formParameters)

  }

  private def getCsrfToken(implicit request: RequestHeader): String = {
    import play.filters.csrf.CSRF

    CSRF.getToken(request).map{ _.value }.getOrElse("")
  }
}

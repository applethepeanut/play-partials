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

import sbt.Keys._
import sbt._

object HmrcBuild extends Build {

  import uk.gov.hmrc.DefaultBuildSettings._
  import uk.gov.hmrc.PublishingSettings._
  import uk.gov.hmrc.{SbtBuildInfo, ShellPrompt}

  val appName = "play-partials"
  val appVersion = "1.4.0-SNAPSHOT"

  lazy val microservice = Project(appName, file("."))
    .settings(version := appVersion)
    .settings(scalaSettings : _*)
    .settings(defaultSettings() : _*)
    .settings(
      targetJvm := "jvm-1.7",
      shellPrompt := ShellPrompt(appVersion),
      libraryDependencies ++= AppDependencies(),
      crossScalaVersions := Seq("2.11.6"),
      resolvers := Seq(
        Opts.resolver.sonatypeReleases,
        Opts.resolver.sonatypeSnapshots,
        "typesafe-releases" at "http://repo.typesafe.com/typesafe/releases/",
        "typesafe-snapshots" at "http://repo.typesafe.com/typesafe/snapshots/"
      )
    )
    .settings(publishAllArtefacts: _*)
    .settings(SbtBuildInfo(): _*)
    .settings(POMMetadata(): _*)
    .settings(Headers(): _ *)
    .disablePlugins(sbt.plugins.JUnitXmlReportPlugin)
    .settings(resolvers += Resolver.bintrayRepo("hmrc", "releases"))
}

private object AppDependencies {

  import play.core.PlayVersion
  import play.PlayImport._

  val compile = Seq(
    filters,
    "com.typesafe.play" %% "play" % PlayVersion.current,
    "com.google.guava" % "guava" % "16.0.1",
    "uk.gov.hmrc" %% "http-verbs" % "1.6.0"
  )

  trait TestDependencies {
    lazy val scope: String = "test"
    lazy val test: Seq[ModuleID] = ???
  }

  object Test {
    def apply() = new TestDependencies {
      override lazy val test = Seq(
        "com.typesafe.play" %% "play-test" % PlayVersion.current % scope,
        "org.scalatest" %% "scalatest" % "2.2.2" % scope,
        "org.pegdown" % "pegdown" % "1.4.2" % scope,
        "org.mockito" % "mockito-all" % "1.9.5" % scope
      )
    }.test
  }

  def apply() = compile ++ Test()
}


object POMMetadata {

  def apply() = {
    pomExtra :=
      <url>https://www.gov.uk/government/organisations/hm-revenue-customs</url>
        <licenses>
          <license>
            <name>Apache 2</name>
            <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
          </license>
        </licenses>
        <scm>
          <connection>scm:git@github.com:hmrc/play-partials.git</connection>
          <developerConnection>scm:git@github.com:hmrc/play-partials.git</developerConnection>
          <url>git@github.com:hmrc/play-partials.git</url>
        </scm>
        <developers>
          <developer>
            <id>xnejp03</id>
            <name>Petr Nejedly</name>
            <url>http://www.equalexperts.com</url>
          </developer>
        </developers>
  }
}

object Headers {
  import de.heikoseeberger.sbtheader.SbtHeader.autoImport._
  def apply() = Seq(
    headers := Map(
      "scala" ->(
        HeaderPattern.cStyleBlockComment,
        """|/*
          | * Copyright 2015 HM Revenue & Customs
          | *
          | * Licensed under the Apache License, Version 2.0 (the "License");
          | * you may not use this file except in compliance with the License.
          | * You may obtain a copy of the License at
          | *
          | *   http://www.apache.org/licenses/LICENSE-2.0
          | *
          | * Unless required by applicable law or agreed to in writing, software
          | * distributed under the License is distributed on an "AS IS" BASIS,
          | * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
          | * See the License for the specific language governing permissions and
          | * limitations under the License.
          | */
          |
          |""".stripMargin
        )
    ),
    (compile in Compile) <<= (compile in Compile) dependsOn (createHeaders in Compile),
    (compile in Test) <<= (compile in Test) dependsOn (createHeaders in Test)
  )
}


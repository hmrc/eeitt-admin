import sbt._
import play.sbt.PlayImport._
import play.core.PlayVersion
import uk.gov.hmrc.SbtAutoBuildPlugin
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin
import uk.gov.hmrc.versioning.SbtGitVersioning

object MicroServiceBuild extends Build with MicroService {

  val appName = "eeitt-admin"

  override lazy val appDependencies: Seq[ModuleID] = compile ++ test()

  val compile = Seq(
    "uk.gov.hmrc" %% "play-reactivemongo" % "6.4.0",
    ws,
    "uk.gov.hmrc" %% "microservice-bootstrap" % "9.1.0", //10.4.0
    "uk.gov.hmrc" %% "play-authorisation" % "5.2.0",
    "uk.gov.hmrc" %% "play-health" % "3.11.0-play-25",
    "uk.gov.hmrc" %% "play-url-binders" % "2.1.0",
    "uk.gov.hmrc" %% "play-config" % "7.3.0",
    "uk.gov.hmrc" %% "logback-json-logger" % "4.4.0",
    "uk.gov.hmrc" %% "domain" % "5.3.0",
    "org.typelevel" % "cats-core_2.11" % "1.6.0"
  )

  def test(scope: String = "test,it") = Seq(
    "uk.gov.hmrc" %% "hmrctest" % "3.5.0-play-25" % scope,
    "org.scalatest" %% "scalatest" % "3.0.5" % scope,
    "org.pegdown" % "pegdown" % "1.6.0" % scope,
    "com.typesafe.play" %% "play-test" % PlayVersion.current % scope
  )

}

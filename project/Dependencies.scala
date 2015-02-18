import sbt._

object Version {
  val akka           = "2.3.9"
  val spray          = "1.3.2"
  val scala          = "2.11.5"
  val slick          = "2.1.0"
  val c3p0           = "0.9.1.2"
  val h2Driver       = "1.4.185"
  val scalaTest      = "2.2.4"
  val spec2          = "2.4.9-scalaz-7.0.6"
  val typesafeConfig = "1.2.1"
  val lockback       = "1.1.2"
}

object Library {
  val akkaActor      = "com.typesafe.akka"  %%  "akka-actor"     % Version.akka
  val sprayCan       = "io.spray"           %%  "spray-can"      % Version.spray
  val sprayRouting   = "io.spray"           %%  "spray-routing"  % Version.spray
  val sprayHttp      = "io.spray"           %%  "spray-http"     % Version.spray
  val sprayHttpx     = "io.spray"           %%  "spray-httpx"    % Version.spray
  val sprayClient    = "io.spray"           %%  "spray-client"   % Version.spray
  val sprayUtil      = "io.spray"           %%  "spray-util"     % Version.spray
  val sprayJson      = "io.spray"           %%  "spray-json"     % "1.3.0"
  val slick          = "com.typesafe.slick" %%  "slick"          % Version.slick
  val c3p0           = "c3p0"               %   "c3p0"           % Version.c3p0
  val h2Database     = "com.h2database"     %   "h2"             % Version.h2Driver
  val akkaTestKit    = "com.typesafe.akka"  %%  "akka-testkit"   % Version.akka  % "test"
  val sprayTestKit   = "io.spray"           %%  "spray-testkit"  % Version.spray % "test"
  val spec2          = "org.specs2"         %%  "specs2"         % Version.spec2 % "test"
  val lockback       = "ch.qos.logback"     % "logback-classic"  % Version.lockback

}

object Dependencies {

  import Library._

  val spraySlick = Seq(
    akkaActor,
    sprayCan,
    sprayRouting,
    sprayHttp,
    sprayHttpx,
    sprayClient,
    sprayUtil,
    sprayJson,
    slick,
    c3p0,
    h2Database,
    akkaTestKit,
    sprayTestKit,
    spec2,
    lockback
  )
}

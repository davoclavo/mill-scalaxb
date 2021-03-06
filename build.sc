// -*- mode: scala -*- 

import mill._, scalalib._, publish._

object scalaxb extends ScalaModule with PublishModule {

  def publishVersion = os.read(os.pwd / "VERSION").trim

  // use versions installed from .tool-versions
  def scalaVersion = scala.util.Properties.versionNumberString
  def millVersion = System.getProperty("MILL_VERSION")

  def artifactName = "mill-scalaxb"

  def m2 = T {
    val pa = publishArtifacts()
    val wd = T.ctx().dest
    val ad = pa.meta.group.split("\\.").foldLeft(wd)((a, b) => a / b) / pa.meta.id / pa.meta.version
    os.makeDir.all(ad)
    pa.payload.map { case (f,n) => os.copy(f.path, ad/n) }
  }

  def pomSettings = PomSettings(
    description = "Scalaxb code generation for mill",
    organization = "io.github.vic",
    url = "https://github.com/vic/mill-scalaxb",
    licenses = Seq(License.`Apache-2.0`),
    versionControl = VersionControl.github("vic", "mill-scalaxb"),
    developers = Seq(
      Developer("vic", "Victor Borja", "https://github.com/vic")
    )
  )

  def compileIvyDeps = Agg(
    ivy"com.lihaoyi::mill-scalalib:${millVersion}",
    ivy"org.scalaxb::scalaxb:1.5.2"
  )

}

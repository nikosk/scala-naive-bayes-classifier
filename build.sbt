name := "pinin"

version := "1.0"

scalaVersion := "2.11.7"


libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4",
  "org.jsoup" % "jsoup" % "1.8.2",
  "de.l3s.boilerpipe" % "boilerpipe" % "1.2.0",
  "xerces" % "xercesImpl" % "2.11.0",
  "net.sourceforge.nekohtml" % "nekohtml" % "1.9.22"
)

resolvers += "Boilerpipe" at "http://boilerpipe.googlecode.com/svn/repo/"

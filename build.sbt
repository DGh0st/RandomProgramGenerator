name := "deep_patel_krunal_patel_anthony_wendling_course_project_cs474"

crossPaths := false
autoScalaLibrary := false

libraryDependencies ++= Seq(
  "org.javassist" % "javassist" % "3.22.0-GA",
  "junit" % "junit" % "4.12" % Test,
)

mainClass in (Compile, run) := Some("Main")
mainClass in (Test, run) := Some("TestRunner")
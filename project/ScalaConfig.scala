import ScalaConfig.commonCompilerOptions
import sbt.Def

object ScalaConfig {
  def compilerOptions(inDevMode: Boolean) = {
    
    Def.setting(commonCompilerOptions
      ++ (if(!inDevMode) Seq("-Xelide-below", "OFF") else Nil)
    )
  
  }

  private val commonCompilerOptions= Seq(
    "-deprecation",
    "-encoding", "UTF-8",
    // Warn when we use advanced language features
    "-feature",
    // Give more information on type erasure warning
    "-unchecked",
    // Don't warn when we use these language features
    "-language:postfixOps", "-language:implicitConversions",
    "-Ywarn-dead-code",
    "-Ywarn-unused",
    "-Xlint",
    // Enable deprecation warnings for features that will be removed from Scala in the future
    "-Xfuture"
  )  


  val version="2.12.8"


}

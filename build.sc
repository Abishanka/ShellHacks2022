import mill._
import mill.scalalib._

object BurstTrieCheckout extends ScalaModule {
  def scalaVersion = "2.11.12"//"2.11.4"
  //def ivyDeps = Agg(ivy"com.nefariouszhen.trie::scala-burst-trie:0.2")
}

object AbiDex extends ScalaModule {
  def scalaVersion = "2.11.12"//"2.11.4"
  def moduleDeps = Seq(BurstTrieCheckout)
  def ivyDeps = Agg(
    //ivy"com.nefariouszhen.trie::scala-burst-trie:0.2",
    ivy"io.circe::circe-core:0.11.2"
  )
  object test extends ScalaModule {
    def scalaVersion = "2.11.12"
    def moduleDeps = Seq(AbiDex)
  }
}

object ARS extends ScalaModule {
  def scalaVersion = "2.11.12"
}

object server extends ScalaModule {
  def scalaVersion = "2.11.12"//"2.11.4"
  def moduleDeps = Seq(BurstTrieCheckout, AbiDex)
  def ivyDeps = Agg(
    ivy"org.springframework:spring-core:5.3.22",
    ivy"org.springframework:spring-beans:5.3.22",
    ivy"org.springframework:spring-context:5.3.22",
    ivy"org.springframework.boot:spring-boot:2.7.3",
    ivy"org.springframework.boot:spring-boot-autoconfigure:2.7.3",
    ivy"javax.servlet:javax.servlet-api:4.0.1",
    ivy"javax.validation:validation-api:2.0.1.Final",
    ivy"org.springframework:spring-web:5.3.22",
    ivy"org.springframework.boot:spring-boot-starter-web:2.7.3"
  )
}
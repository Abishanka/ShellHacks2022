import mill._
import mill.scalalib._

object BurstTrie extends ScalaModule {
  def scalaVersion = "2.11.12"//"2.11.4"
  //def ivyDeps = Agg(ivy"com.nefariouszhen.trie::scala-burst-trie:0.2")
}

object Trie extends ScalaModule {
  def scalaVersion = "2.11.12"//"2.11.4"
  def moduleDeps = Seq(BurstTrie)
  def ivyDeps = Agg(
    //ivy"com.nefariouszhen.trie::scala-burst-trie:0.2",
    ivy"io.github.zamblauskas::scala-csv-parser:0.13.1",
  )
}
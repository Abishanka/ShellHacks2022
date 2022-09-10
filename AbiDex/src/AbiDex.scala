import com.nefariouszhen.trie.BurstTrie
import java.io._
import zamblauskas.csv.parser._
import scala.io.Source


case class Security(
    security_id:String,
    sedol:Option[String],
    isin:Option[String],
    ric:Option[String],
    bloomberg:Option[String],
    bbg:Option[String],
    symbol:Option[String],
    root_symbol:Option[String],
    bb_yellow:Option[String],
    spn:Option[String])

object Main extends App {
    val suffixMap = BurstTrie.newSuffixMap[Int]()
    suffixMap.put("banana",5)
    suffixMap.put("ananas",56)
    suffixMap.put("ban",566)
    for (result <- suffixMap.query("ananas")) println(result)

    //val baos = new ByteArrayOutputStream(1024)
    //val o = new ObjectOutputStream(baos)
    //o.writeObject(suffixMap)

    val filename = "test.csv"
    val fileContents = Source.fromFile(filename).getLines.mkString("\n")

    val results = Parser.parse[Security](fileContents)
    
    for{
        good_results <- results.right
        result <- good_results
    }{
        //println(result)
    }

    for{
        j <- List( List(1,2,3), List(5,6,7)  )
        i <- j
    }{

        //println(j)
        println(i)
    }
    

}
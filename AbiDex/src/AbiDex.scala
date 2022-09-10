import com.nefariouszhen.trie.BurstTrie
import java.io._
import zamblauskas.csv.parser._
import scala.io.Source
import zamblauskas.csv.parser.util.CsvReaderUtil._

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
    val suffixMaps = Array.tabulate(11)( (i) => BurstTrie.newSuffixMap[ Int ]())

    //val baos = new ByteArrayOutputStream(1024)
    //val o = new ObjectOutputStream(baos)
    //o.writeObject(suffixMap)

    val filename = "Securities - Schonfeld ShellHacks.csv"
    val fileSource = Source.fromFile(filename).getLines.toList
    val header = fileSource.head.split(",")
    val fileContents:Array[String] = fileSource.tail.take(1000).toArray
    val data =
        for{
           line <- fileContents
        }yield for {
          field <- line.split(",")
        }yield field match {
                case "" => None
                case s => Some(s)
            }
    
    
    for{
        i <- 0 to data.length - 1
        j <- 0 to suffixMaps.length - 1
        if(j < data(i).length)
        datum <- data(i)(j)
    }{  
        if( i % 10000 == 0 && j == 0) println ( (1.0*i) / data.length )
        suffixMaps(j).put( datum, i )
    }
    import scala.collection.mutable
    def collect[K,V](seq:Seq[(K,V)]):mutable.HashMap[K,List[V]] = {
        var out = new mutable.HashMap[K,List[V]]()
        for( (k,v) <- seq ){
            out.get(k) match {
                case None => out += k -> (v :: Nil)
                case Some(list) => out += k -> (v :: list)
            }
        }
        out
    }

    def query(q:String) = {
        val exactMatches = 
            collect(for{
                i <- 0 to header.length - 1
                security_id <- suffixMaps(i).query(q)
                //need to add test for this, might be bug here
                street_id <- data(security_id)(i)
                if(street_id == q)
            }yield {
                //println(street_id,q,street_id)
                (security_id,i)
            })
        val partialMatches = 
            collect(for{
                i <- 0 to header.length - 1
                security_id <- suffixMaps(i).query(q)
                //need to add test for this, might be bug here
                street_id <- data(security_id)(i)
                if(street_id != q)
            }yield {
                //println(street_id,q,street_id)
                (security_id,i)
            })
        (exactMatches,partialMatches)
    }

    //for( security <- suffixMaps(6).query("19N3N") ) println( data(security).mkString(",") )
    println(query("19N3N"))
    println(header.mkString(","))

    val sc = new java.util.Scanner(System.in)
    val s = sc.nextLine()
    
/*
    val results = Parser.parse[Security](fileContents)
    println(results)
    println("done parsing")
    for{
        good_results <- results.right
        result <- good_results
        bbg <- result.bbg
    }{
<<<<<<< HEAD
        
        suffixMap.put(bbg,result)
=======
        //println(result)
>>>>>>> 74c6363a92048b35d2c48be205f1cf1db3b49e08
    }
    for( security <- suffixMap.query("19N3N") )println(security)*/

    for{
        j <- List( List(1,2,3), List(5,6,7)  )
        i <- j
    }{

        //println(j)
        println(i)
    }
    

}
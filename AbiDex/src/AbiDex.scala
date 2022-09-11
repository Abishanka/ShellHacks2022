import com.nefariouszhen.trie.BurstTrie
import java.io._
import scala.io.Source
import scala.collection.mutable


case class PastQuery(
    query:String,
    //maps from the string form of the security_id to the frequency that that security_id was picked as the final
    frequency_map:mutable.HashMap[String,Double]
)

object AbiDex extends App {

    val defaultPriorities = Array(
        "root_symbol",        
        "bbg",
        "symbol",
        "ric",
        "cusip",
        "isin",
        "bb_yellow",
        "bloomberg",
        "spn",
        "security_id",
        "sedol"
    )

    val suffixMaps = Array.tabulate(11)( (i) => BurstTrie.newSuffixMap[ Int ]())

    val filename = "Securities - Schonfeld ShellHacks.csv"
    val fileSource = Source.fromFile(filename).getLines.toList
    val header = fileSource.head.split(",")
    val defaultWeights = for(symbol <- header) yield (header.length - defaultPriorities.indexOf(symbol) ).toDouble
    for(i <- 0 to 10){
        println(defaultWeights(i) , header(i) )
    }


    val fileContents:Array[String] = fileSource.tail.take(100).toArray
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
                (security_id,i)
            })
        val partialMatchesBySecurityID = 
            collect(for{
                i <- 0 to header.length - 1
                security_id <- suffixMaps(i).query(q)
                //need to add test for this, might be bug here
                street_id <- data(security_id)(i)
                if(street_id != q)
            }yield {
                (security_id,i)
            })
        val partialMatchesByStreetCategory = 
            collect(for{
                i <- 0 to header.length - 1
                security_id <- suffixMaps(i).query(q)
                //need to add test for this, might be bug here
                street_id <- data(security_id)(i)
                if(street_id != q)
            }yield {
                (i,security_id)
            })
        
        (exactMatches,partialMatchesBySecurityID,partialMatchesByStreetCategory)
    }

    def sortPartials(
        partialMatchesBySecurityID:mutable.HashMap[Int,List[Int]],
        partialMatchesByStreetCategory:mutable.HashMap[Int,List[Int]],
        weights:Array[Double]) = {
        //KALASHNIKOV ALG 1, GIVE ALL BOIS EQUAL WEIGHT
        val probabilities = (for{
            (security_id,street_categories) <- partialMatchesBySecurityID
        }yield ( security_id,(for(cat <- street_categories) yield weights(cat)).reduceLeft( (x,y) => Math.max(x,y) ))).toList.sortBy( t => -t._2 )
        probabilities
    }

    val (exactMatches,partialMatchesBySecurityID,partialMatchesByStreetCategory) = query("96")
    val priority_list = sortPartials(partialMatchesBySecurityID,partialMatchesByStreetCategory,defaultWeights)
    




    println(header.mkString(","))

    val sc = new java.util.Scanner(System.in)
    val s = sc.nextLine()
    

}
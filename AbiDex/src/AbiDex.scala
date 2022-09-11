package abi.dex

import com.nefariouszhen.trie.BurstTrie
import java.io._
import scala.io.Source
import scala.collection.mutable


case class PastQuery(
    query:String,
    //maps from the string form of the security_id to the frequency that that security_id was picked as the final
    frequency_map:mutable.HashMap[String,Double]
)

package object AbiDex extends {

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
        //println(defaultWeights(i) , header(i) )
    }
    private var fileContents:Array[String] = Array()
    private var data:Array[Array[Option[String]]] = Array()
    def init(dirty: Boolean = false,dirty_count: Int = 5000, verbose: Boolean = false) = {

        fileContents = if(dirty) fileSource.tail.take(dirty_count).toArray else fileSource.tail.toArray
        data =
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
            if( i % 10000 == 0 && j == 0 && verbose) println ( (1.0*i) / data.length )
            suffixMaps(j).put( datum, i )
        }
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

    def internal_query(q:String) = {
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
        val id_and_probabilities = (for{
            (security_id,street_categories) <- partialMatchesBySecurityID
        }yield ( security_id,(for(cat <- street_categories) yield weights(cat)).reduceLeft( (x,y) => Math.max(x,y) ))).toList.sortBy( t => -t._2 )
        id_and_probabilities
    }

    def query(q:String,debug_string:String = "",count:Int = 10):Array[Array[String]] = {
        val (exactMatches,partialMatchesBySecurityID,partialMatchesByStreetCategory) = internal_query(q)
        val (security_ids,probabilities) = sortPartials(partialMatchesBySecurityID,partialMatchesByStreetCategory,defaultWeights).unzip
        
        (for( security_id <- security_ids.toArray )yield{
            (for{ 
                field <- data(security_id)
            }yield field.getOrElse(debug_string)) ++ Array.tabulate(header.length - data(security_id).length)( (i) => debug_string )
        }).take(count)
    }

    def queryJSONString(q:String,count:Int = 10):String = {
        (for( result <- query(q,count = count) )yield (for( (head,key) <- header.zip(result) ) yield s""""$head":"${key}"""" ).mkString("{",",","}")).mkString("[",",","]")
    }

    





    
    

}
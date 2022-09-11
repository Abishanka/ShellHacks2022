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
    val defaultWeights = for(symbol <- header) yield (header.length - defaultPriorities.indexOf(symbol) )*(1.0/12)
    var bestWeights = defaultWeights.clone()
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
            suffixMaps(j).put( datum.toUpperCase() , i )
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

    def internal_query(qq:String,debug:Boolean = false) = {
        var q = qq.toUpperCase()
         
            val exactMatches = 
                collect(for{
                    i <- 0 to header.length - 1
                    security_id <- {/*println(q,suffixMaps(i).query(q));*/suffixMaps(i).query(q)}
                    //need to add test for this, might be bug here
                    street_id <- {/*println(data(security_id)(i));*/data(security_id)(i)}
                    if(street_id.toUpperCase() == q)
                }yield {
                    (security_id,i)
                })
            val partialMatchesBySecurityID = 
                collect(for{
                    i <- 0 to header.length - 1
                    security_id <- suffixMaps(i).query(q)
                    //need to add test for this, might be bug here
                    street_id <- data(security_id)(i)
                    if(street_id.toUpperCase() != q)
                }yield {
                    (security_id,i)
                })
            val partialMatchesByStreetCategory = 
                collect(for{
                    i <- 0 to header.length - 1
                    security_id <- suffixMaps(i).query(q)
                    //need to add test for this, might be bug here
                    street_id <- data(security_id)(i)
                    if(street_id.toUpperCase() != q)
                }yield {
                    (i,security_id)
                })
            if(debug){
                println(exactMatches)
                println(partialMatchesBySecurityID)
                println(partialMatchesByStreetCategory)
            }
        (exactMatches,partialMatchesBySecurityID,partialMatchesByStreetCategory)
    }

    def sortPartials(
        partialMatchesBySecurityID:mutable.HashMap[Int,List[Int]],
        partialMatchesByStreetCategory:mutable.HashMap[Int,List[Int]],
        weights:Array[Double]) = {
        //KALASHNIKOV ALG 1, GIVE ALL BOIS EQUAL WEIGHT
        val id_and_probabilities = (for{
            (security_id,street_categories) <- partialMatchesBySecurityID
        }yield ( security_id,(for(cat <- street_categories) yield weights(cat)  ).reduceLeft( (x,y) => Math.max(x,y) ))).toList.sortBy( t => -t._2 )
        val softsum = (id_and_probabilities.unzip._2).sum
        val prob = for( p <- id_and_probabilities.unzip._2)yield p/softsum
        (id_and_probabilities.unzip._1,prob)
    }

    def query(q:String,debug_string:String = "",count:Int = 10):Array[Array[String]] = {
        val (exactMatches,partialMatchesBySecurityID,partialMatchesByStreetCategory) = internal_query(q)
        val (security_ids,probabilities) = sortPartials(partialMatchesBySecurityID,partialMatchesByStreetCategory,defaultWeights)
        
        val part = (for( security_id <- security_ids.toArray )yield{
            (for{ 
                field <- data(security_id)
            }yield field.getOrElse(debug_string)) ++ Array.tabulate(header.length - data(security_id).length)( (i) => debug_string )
        }).take(count)
        val exact = (for( security_id <- exactMatches.toArray )yield{
            (for{ 
                field <- data(security_id._1)
            }yield field.getOrElse(debug_string)) ++ Array.tabulate(header.length - data(security_id._1).length)( (i) => debug_string )
        }).take(count)
        if( exact.length == 0 ) part else exact
    }

    def queryJSONString(q:String,count:Int = 10):String = {
        (for( result <- query(q,count = count) )yield (for( (head,key) <- header.zip(result) ) yield s""""$head":"${key}"""" ).mkString("{",",","}")).mkString("[",",","]")
    }

    
    import abi.cel.CEL
    import abi.rs.ARS
    def trainModel( dataa:List[PastQuery],threshold:Int ) = {
        
        def lambda(weights:Array[Double],best_Loss:Double):Double = {
            var loss = 0.0
            for( PastQuery(query,frequency_map) <- dataa ){
                val (exactMatches,partialMatchesBySecurityID,partialMatchesByStreetCategory) = internal_query(query)
                val (security_ids,probabilities) = sortPartials(partialMatchesBySecurityID,partialMatchesByStreetCategory,weights)
                val scc_name = for( id <- security_ids ) yield data(id)(0).getOrElse("")
                val prob = mutable.HashMap() ++= (scc_name.zip(probabilities)).take(10)
                
                loss = loss + CEL.CE(frequency_map,prob)
                if(loss > best_Loss) return loss
            }
            loss
        }
        val prev = lambda(bestWeights,Double.MaxValue)
        bestWeights = ARS.run( 11,(x:Array[Double],y:Double) => lambda(x,y), threshold = threshold )
        (bestWeights.mkString(","),prev,lambda(bestWeights,Double.MaxValue))
    }





    
    

}

import abi.dex.AbiDex
import abi.dex.PastQuery

object Test extends App {

    AbiDex.init(dirty = true,dirty_count=25000,verbose=true)


    AbiDex.internal_query("BBG001FC63F5")
    for( result <- AbiDex.query("BBG001FC63F") ){
        println(result.mkString(","))
    }

    println( AbiDex.queryJSONString("BBG001FC63F5",count = 10) )
    val sc = new java.util.Scanner(System.in)
    val s = sc.nextLine()

import scala.io.Source

    //TRAINING
    if(true){
        val filename = "cusip_data_2.csv"
        val fileSource = Source.fromFile(filename).getLines.toList
        val training_data = (for(line <- fileSource)yield line.split(",").toList match {
            case q :: sec_id :: Nil => {
                PastQuery(q, scala.collection.mutable.HashMap( sec_id -> 1.0 ) )
            }
        }).toList
        println(AbiDex.trainModel(training_data,1000))
    }
    
}
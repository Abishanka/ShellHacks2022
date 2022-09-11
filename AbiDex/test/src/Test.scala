
import abi.dex.AbiDex

object Test extends App {

    AbiDex.init(dirty = true,dirty_count=1000)


    AbiDex.internal_query("BBG001FC63F5")
    for( result <- AbiDex.query("BBG001FC63F") ){
        println(result.mkString(","))
    }

    println( AbiDex.queryJSONString("BBG001",count = 10) )
    val sc = new java.util.Scanner(System.in)
    val s = sc.nextLine()
}

import abi.dex.AbiDex

object Test extends App {

    AbiDex.init(dirty = true,dirty_count=100)

    for( result <- AbiDex.query("") ){
        println(result.mkString(","))
    }

    println( AbiDex.queryJSONString("",count = 2) )

    val sc = new java.util.Scanner(System.in)
    val s = sc.nextLine()
}
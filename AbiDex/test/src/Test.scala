


object Test extends App {

    AbiDex.init(dirty = true,dirty_count=100)

    for( result <- AbiDex.query("09") ){
        println(result.mkString(","))
    }

    println( AbiDex.queryJSONString("09") )

    val sc = new java.util.Scanner(System.in)
    val s = sc.nextLine()
}
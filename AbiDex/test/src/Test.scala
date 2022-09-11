


object Test extends App {

    AbiDex.init(dirty = true)

    for( result <- AbiDex.query("09") ){
        println(result.mkString(","))
    }

    val sc = new java.util.Scanner(System.in)
    val s = sc.nextLine()
}
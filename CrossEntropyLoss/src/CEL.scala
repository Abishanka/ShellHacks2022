import Math.log10
import scala.collection.mutable

object CEL extends App {
  //import scala.math._
  // calculate cross entropy
  def cross_entropy(P: Array[Double], Q: Array[Double]): Double = {
    var sum = 0.0
    for(i <- 0 to P.length -1){
      sum = sum +(P(i) * (log10(Q(i))/log10(2)))
    }
    return sum * -1
  }
 // return -sum([p[i]*log2(q[i]) for i in range(len(p))])

//  var p = Array(0.0, 0.0, 0.5, 0.5)
//  var q = Array(0.5, 0.5, 0.0, 0.0)
//
//  println(cross_entropy(p,q))
  var s = mutable.HashMap( "cat" -> 0.7, "dog" -> 0.1, "hat" -> 0.1, "bog" -> 0.1 )
  var t = mutable.HashMap( "dog" -> 0.1, "hat" -> 0.1, "cat" -> 0.7, "bog" -> 0.1 )
  def CE(s: mutable.HashMap[String,Double], t: mutable.HashMap[String,Double]) : Double ={
    //var s_array = Array[Double](s.length)
    //var t_array = Array[Double](t.length)
    var s_array = s.toList.sortBy( (tuple) => tuple._1 ).unzip._2.toArray
    var t_array = t.toList.sortBy( (tuple) => tuple._1 ).unzip._2.toArray

    println(s_array.mkString(","))
    println(t_array.mkString(","))
    var result = cross_entropy(s_array, t_array)
    print(result)
    return result
  }
  CE(s,t)

}
import Math.log10
import scala.collection.mutable

object CEL extends App {
  //import scala.math._
  // calculate cross entropy

  //T IS TRUTH, P IS CALCULATED PROBs
  def cross_entropy(T: Array[Double], P: Array[Double]): Double = {
    var sum = 0.0
    for(i <- 0 to P.length -1){
      sum = sum +(T(i) * (log10(P(i))/log10(2)))
    }
    return sum * -1
  }
 // return -sum([p[i]*log2(q[i]) for i in range(len(p))])

//  var p = Array(0.0, 0.0, 0.5, 0.5)
//  var q = Array(0.5, 0.5, 0.0, 0.0)
//
//  println(cross_entropy(p,q))
  var t = mutable.HashMap( "cat" -> 0.75, "dog" -> 0.0, "hat" -> 0.0, "bog" -> 0.25 )
  var p = mutable.HashMap( "dog" -> 0.0125, "hat" -> 0.0125, "cat" -> 0.75, "bog" -> 0.225 )

  //TRUTH and PROB
  def CE(t: mutable.HashMap[String,Double], p: mutable.HashMap[String,Double]) : Double ={
    var sum = 0.0
    for{
      (k,pi) <- p
      ti <- t.get(k)
    }{
      sum += ti * log10(pi)/log10(2)
    }

    return -1.0*sum
  }
  println( CE(t,p) )

}
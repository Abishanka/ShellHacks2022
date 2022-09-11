
object ARS extends App {
  var calc_loss = (x:Array[Double], eval: Double) => {
//    (for( xs <- x)yield (xs-.5)*(xs-.5)).toList.sum
    var sum = 0.0
    var compute = true
    for (i <- 0 to x.length - 1) if(compute){
      sum += x(i)
      if(sum >= eval){
        compute = false
      }
    }
    sum
  }

 // println("The two random Values", ran_1," ", ran_2)
  var n = 100
  var best_Sample = new Array[Double](n)
  for (i <- 0 to n -1){
    best_Sample(i) = Math.random()
  }
   var best_Loss = 999999999.0
   var shrink = 0
   var threshold = 1000
   while(shrink < threshold){
    var ran_array = new Array[Double](n)
    for(i <- 0 to n-1){
      ran_array(i) = ((Math.random()-0.5)*2) * ((Math.pow(2, -(shrink/n))))
    }
    var sample = best_Sample.zip(ran_array).map { case (x, y) => x + y }
    //clamping values
    for(i <- 0 to n-1){
     if(sample(i) > 1){
      sample(i) = 1
     }
     if(sample(i) < 0){
      sample(i) = 0
     }
    }
   var eval = calc_loss(sample, best_Loss)
   if(eval < best_Loss){
    best_Loss = eval
    best_Sample = sample
    shrink = 0
   } else{
    shrink = shrink + 1
     }
   }

  println(best_Sample.mkString(","))
  println(best_Loss)
}
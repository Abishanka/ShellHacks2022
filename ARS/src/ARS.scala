
object ARS extends App {
  var calc_loss = (int1:Double, int2:Double) => Math.pow(int1,2) + Math.pow(int2,2)

 // println("The two random Values", ran_1," ", ran_2)
  var n = 2
  var best_Sample = new Array[Double](2)
  best_Sample(0) =  Math.random()
  best_Sample(1) =  Math.random()
//  println(Best_Sample.mkString(","))
//  var one = Best_Sample(0)
   var best_Loss = 999999999.0
   var shrink = 0
   var threshold = 10
   while(shrink < threshold){
    var ran_array = new Array[Double](n)
    for(i <- 0 to n-1){
      ran_array(i) = ((Math.random()-0.5)*2) * (1/(Math.pow(2, (1/n))))
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
   var eval = calc_loss(sample(0), sample(1))
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
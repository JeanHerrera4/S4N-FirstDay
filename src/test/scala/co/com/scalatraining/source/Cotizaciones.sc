var registro:List[List[String]] = List(
  List("2018/07","s4n","10","1000000"),
  List("2018/07","s4n","20","1000000"),
  List("2018/08","s4n","30","2000000"),
  List("2018/09","s4n","30","0"))

println(registro)

registro.foreach((x)=>
    println(x.reverse.head)
)

def valorTotal(dias:Int, dinero:Int):Int = {
  val total = (30*dinero)/dias
  total
}

registro.foreach((x)=>
    x.head.groupBy(f = x => x).sum(x.reverse.head.toInt)
  /*if(x.head.groupBy()){
    registro.drop(1)
    texto.split(" ").groupBy(f = x => x).mapValues(_.size)
  }*/
)




//var registroFinal:List[List[String]]

registro.foreach((x)=>
  if(x.reverse.head=="1000000"){
    registro.drop(1)
  }
)

println(registro)



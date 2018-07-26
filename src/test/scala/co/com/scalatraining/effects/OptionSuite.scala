package co.com.scalatraining.effects

import org.scalatest.FunSuite

import scala.collection.immutable.Seq

class OptionSuite extends FunSuite {

  test("Se debe poder crear un Option con valor"){
    val s = Option{ //Constructor de tipo
      1
    }
    assert(s == Some(1))
  }

  test("Se debe poder crear un Some con valor"){
    val s = Some{
      1
    }
    assert(s == Some(1))
  }

  /*test("Se debe poder crear un Some sin valor"){
    val s = Some{
    }
    assert(s == Some(null))
  }

  test("Se debe poder crear un Option sin valor"){
    val s = Option{
    }
    assert(s == Some(null))
  }*/

  test("Se debe poder crear un Option para denotar que no hay valor"){
    val s = None
    assert(s == None)
  }

  test("Es inseguro acceder al valor de un Option con get"){
    val s = None
    assertThrows[NoSuchElementException]{
      val r = s.get
    }

  }

  test("Se debe poder hacer pattern match sobre un Option") {
    val lista: Seq[Option[String]] = List(Some("Andres"), None, Some("Luis"), Some("Pedro"))
    val nombre: Option[String] = lista(1)
    var res = ""
    res = nombre match {
      case Some(nom) => nom
      case None => "NONAME"
    }
    assert(res == "NONAME")
  }

  test("Fold en Option"){
    val o = Option(1)

    //En caso de ser None tome 10, si es Some x => x +20
    val res: Int = o.fold{
      10
    }{
      x => x + 20
    }

    assert(res == 21)
  }

  test("Fold en Option null"){
    //val o:Option[Int] = Option(null)

    def foo(s:Int):Option[Int]=
      if(s % 2 == 0){
         Some(s)
      }else{
        None
      }

    //En caso de ser None tome 10, si es Some x => x +20
    val res: Int = foo(2).fold{
      10
    }{
      x => x + 20
    }

    assert(res == 22)
  }

  test("Se debe poder saber si un Option tiene valor con isDefined") {
    val lista = List(Some("Andres"), None, Some("Luis"), Some("Pedro"))
    val nombre = lista(0)
    assert(nombre.isDefined)
  }

  test("Se debe poder acceder al valor de un Option de forma segura con getOrElse") {
    val lista = List(Some("Andres"), None, Some("Luis"), Some("Pedro"))
    val nombre = lista(1)
    val res = nombre.getOrElse("NONAME")
    assert(res == "NONAME")
  }

  test("getOrElse como un fold"){

    val nombre = Some("Andres")
    def foo(x:Option[String]) = x.fold{"NONAME"}{y => y}

    val res = nombre.getOrElse("NONAME")
    val res2 = foo(nombre)

    assert(res == res2)

  }

  test("Un Option se debe poder transformar con un map") {
    val lista = List(Some("Andres"), None, Some("Luis"), Some("Pedro"))
    val nombre = lista(0)
    val nombreCompleto: Option[String] = nombre.map(s => s + " Felipe")
    assert(nombreCompleto.getOrElse("NONAME") == "Andres Felipe")
  }

  test("Un Option se debe poder transformar con flatMap en otro Option") {
    val lista = List(Some("Andres"), None, Some("Luis"), Some("Pedro"))
    val nombre = lista(0)

    val resultado: Option[String] = nombre.flatMap(s => Option(s.toUpperCase))
    resultado.map( s => assert( s == "ANDRES"))
  }

  test("Un Option se debe poder filtrar con una hof con filter") {
    val lista = List(Some(5), None, Some(40), Some(20))
    val option0 = lista(0)
    val option1 = lista(1)
    val res0 = option0.filter(_>10)
    val res1 = option1.filter(_>10)

    assert(res0 == None)
    assert(res1 == None)
  }

  test("Un Option se debe poder filtrar con una hof con filter x2") {
    val lista = List(Some(5), None, Some(40), Some(20))
    val option0 = lista(0)
    val option1 = lista(1)
    val res0 = option0.filter(_>10)
    val res1 = option1.filter(_>10)
    val res2 = option0.filter(_<10)


    assert(res0 == None)
    assert(res1 == None)
    assert(res2 == Some(5))
  }

  test("for comprehensions en Option") {
    val lista = List(Some(5), None, Some(40), Some(20))
    val s1 = lista(0)
    val s2 = lista(2)

    // for ... yield,
    val resultado = for {
      x <- s1
      y <- s2
    } yield x+y

    assert(resultado == Some(45))
  }

  test("for comprehensions en Option con 3") {
    val lista = List(Some(5), None, Some(40), Some(20))
    val s1 = lista(0)
    val s2 = lista(2)
    val s3 = lista(3)

    // for ... yield,
    val resultado = for {
      x <- s1
      y <- s2
      z <- s3
    } yield x+y+z

    assert(resultado == Some(65))
  }

  //String Interpolation buscar

  test("for comprenhension yield"){

    def foo(y:Int): Option[Int] = {
      println(s"Ejecutando a foo con $y")
      Some(y)
     }

    def bar(x:Int) = {
      println(s"ejectuando a bar con $x")
      None
    }

    //Todo efecto tiene proyección que interrumpe una cadena
    val resultado = for {
      x <- foo(5)
      z <- foo(5)
      z <- foo(5)
      z <- foo(5)
      z <- foo(5)
      z <- foo(5)
      z <- foo(5)
      z <- foo(5)
      z <- foo(5)
      y <- bar(4)
    } yield x+y+z

    assert(resultado == None)
  }

  test("Con 3 Some utilizamos flatMap"){
    val o1 = Some(1)
    val o2 = Some(2)
    val o3 = Some(3)

    val res = o1.flatMap(x =>
              o2.flatMap(y =>
                o3.flatMap(z => Option(x+y+z))))

    assert(res == Some(6))
  }


  test("for comprehesions None en Option") {
    val consultarNombre = Some("Andres")
    val consultarApellido = Some("Estrada")
    val consultarEdad = None
    val consultarSexo = Some("M")



    val resultado = for {
      nom <- consultarNombre
      ape <- consultarApellido
      eda <- consultarEdad
      sex <- consultarSexo
    //} yield (nom+","+ape+","+eda+","+sex)
    } yield (s"$nom $ape, $eda,$sex")

    assert(resultado == None)
  }

  test("for comprehesions None en Option 2") {

    def consultarNombre(dni:String): Option[String] = Some("Felix")
    def consultarApellido(dni:String): Option[String] = Some("Vergara")
    def consultarEdad(dni:String): Option[String] = None
    def consultarSexo(dni:String): Option[String] = Some("M")

    val dni = "8027133"
    val resultado = for {
      nom <- consultarNombre(dni)
      ape <- consultarApellido(dni)
      eda <- consultarEdad(dni)
      sex <- consultarSexo(dni)
    //} yield (nom+","+ape+","+eda+","+sex)
    } yield (s"$nom $ape, $eda,$sex")

    assert(resultado == None)
  }

  test("Tony Morris flatMap"){

    def foo(x:String) = Some(x)

    val option: Option[String] = Some("Jean")
    val res = option.flatMap(foo(_))

    assert(res == Some("Jean"))

    val res2 = option match {
      case None => None
      case Some(x) => foo(x)
    }

    assert(res2 == Some("Jean"))

  }

  test("Tony Morris flatten"){

    def foo(x:String) = Some(x)

    val o = Some(Some("Jean"))
    val res = o.flatten
    assert(res == Some("Jean"))

  }

  test("Tony Morris map"){

    def foo(x:String) = Some(x)

    val option: Option[String] = Some("Jean")
    val res = option.map(foo(_))
    assert(res == Some(Some("Jean")))

  }

  test("Tony Morris foreach"){

    def foo(x:Int):Some[Int] = Some(x)



    val option: Option[Int] = Some(1)
    val res = option.foreach(foo(_))
    assert(res == Some(1))
  }

  test("Tony Morris isDefined"){

    val option: Option[Int] = Some(1)
    val res = option.isDefined
    assert(res == true)

  }

  test("Tony Morris isEmpty"){

    val option: Option[Int] = Some(1)
    val res = option.isEmpty
    assert(res == false)
  }

  test("Tony Morris forall"){

    def foo(s:Int)=
      if(s % 2 == 0){
        true
      }else{
        false
      }

    val option: Option[Int] = None
    val res = option.forall(foo(_))
    assert(res == true)
  }

  test("Tony Morris exists"){

    def foo(s:Int)=
      if(s % 2 == 0){
        true
      }else{
        false
      }

    val option: Option[Int] = Some(4)
    val res = option.forall(foo(_))
    assert(res == true)
  }

  test("Tony Morris orElse"){

    def foo(x:Int):Some[Int] = Some(x)
    val option: Option[Int] = Some(1)
    val res = option.orElse(foo(2))
    assert(res == Some(1))
  }

  test("Tony Morris getOrElse"){

    def foo(x:Int):Some[Int] = Some(x)
    val option: Option[Int] = None
    val res = option.getOrElse(foo(_))
    assert(res == Some(1))
  }

}


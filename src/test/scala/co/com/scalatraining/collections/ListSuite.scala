package co.com.scalatraining.collections

import org.scalatest.FunSuite

import scala.collection.immutable.Seq

/**
  * Hasta Scala 2.12 la taxonomía de colecciones es la siguiente:
  *
  * Para inmutables:
  * https://docs.scala-lang.org/resources/images/collections.immutable.png
  *
  * Para mutables:
  * https://docs.scala-lang.org/resources/images/collections.mutable.png
  *
  * La documentación de Scala para colecciones la puede encontrar en:
  * https://docs.scala-lang.org/overviews/collections/introduction.html
  *
  * El api doc de colecciones lo encuentra en:
  *
  * https://www.scala-lang.org/api/current/scala/collection/index.html
  */
class ListSuite extends FunSuite {

  test("Una List se debe poder construir") {

    val lista: List[Int] = List(1, 2, 3, 4)
    val lista2 = 1 :: 2 :: 3 :: 4 :: Nil
    assert(lista == lista2)
  }

  test("Se puede hacer una lista de un solo tipo"){
    assertDoesNotCompile( "val l = List[String](\"1\", \"2\", 3)")
  }


  test("Una lista se debe poder recorrer imperativamente") {
    val lista = List(1, 2, 3, 4)
    assertResult(10) {
      var sum = 0
      lista.foreach((x) =>
        sum += x
      )
      sum
    }
  }


  test("A una lista se le debe poder adicionar elementos"){
    val l1 = List(1,2,3)
    val l2 = 4::l1
    assert(l2 == List(4,1,2,3))
  }

  test("Se puede concatenar listas"){
    val l1 = List(1,2)
    val l2 = List(3,4)
    val l3 = l1 ::: l2
    assert(l3 == List(1,2,3,4))
  }

  test("Adiccionar un elemento con:+"){
    val l1 = List(1,2)
    val l2 = l1:+3
    assert(l2 == List(1,2,3))
  }

  test("Adiccionar con append y prepend, 1 y 5"){
    val l1 = List(2,3,4)
    val l2 = 1 :: (l1 :+5)
    assert(l2 == List(1,2,3,4,5))
  }


  test("A una lista se le debe poder eliminar elementos con drop") {
    val lista = List(1, 2, 3, 4)
    val dropped =lista.drop(2)

    assertResult(List(3, 4)) {
      dropped
    }
  }

  test("A una lista se le pueden descartar elementos en una direccion determinada (right)") {
    val lista = List(1, 2, 3, 4)
    assertResult(List(1, 2)) {
      lista.dropRight(2)
    }
  }

  test("Se debe poder consultar el primer elemento de una lista de forma insegura") {
    val lista = List(1, 2, 3, 4)
    assertResult(1) {
      lista.head
    }
  }

  test("Que pasa si hacemos head a una List()") {
    val lista = List()
    assertThrows[NoSuchElementException] {
      lista.head
    }
  }

  test("Se debe poder obtener todos los elementos de una lista sin el primer elemento") {
    val lista = List(1, 2, 3, 4)
    assertResult(List(2, 3, 4)) {
      lista.tail
    }
  }

  test("Que pasa si hacemos tail a un List()") {
    val lista = List()
    assertThrows[UnsupportedOperationException] {
      val res =  lista.tail
    }
  }

  test("Una lista se debe poder filtrar con una hof") {
    val lista = List(1, 2, 3, 4)
    assertResult(List(2, 4)) {
      lista.filter(numero =>
        numero % 2 == 0
      )

      lista.filter(_%2==0) //Tratar de no usar

    }
  }

  test("DropWhile"){
    val l = List(1, 2, 3, 4, 5)
    var r = l.dropWhile(x => x%2!=0)
    println(r)
    assert(r==List(2,3,4,5))
    val r2 = l.dropWhile(x => x%2==0)
    println(r2)
    assert(r2 == List(1,2,3, 4, 5))
  }


  test("Una lista se debe poder acumular") {
    val lista = List(1, 2, 3, 4, 5)
    assertResult(15) {
      lista.fold(0) { (acumulado, item) =>
        acumulado + item
      }
    }
  }

  test("Una lista se debe poder multiplicar") {
    val lista = List(1, 2, 3, 4, 5)
    assertResult(120) {
      lista.fold(1) { (acumulado, item) =>
        acumulado * item
      }
    }
  }

  test("Una lista se debe poder acumular en una direccion determinada (izquierda)") {
    val lista = List("Andres", "Felipe", "Juan", "Carlos")
    assertResult("1.Andres,2.Felipe,3.Juan,4.Carlos,") {
      var cont = 0
      lista.foldLeft("") { (resultado, item) =>
        cont = cont + 1
        resultado + cont + "." + item + ","
      }
    }
  }

  test("Una lista se debe poder acumular en una direccion determinada (derecha)") {
    val lista = List("Andres", "Felipe", "Juan", "Carlos")
    assertResult("1.Carlos,2.Juan,3.Felipe,4.Andres,") {
      var cont = 0
      lista.foldRight("") { (item, resultado) =>
        cont = cont + 1
        resultado + cont + "." + item + ","
      }
    }
  }



  test("fold sobre una List de objetos"){
    case class MyCaseClass(i:Int, var s:String)
    val lista: List[MyCaseClass] = List( MyCaseClass(1,"1"),  MyCaseClass(2, "2"))

    assertResult("12"){
      lista.map(x=>x.s).fold(""){(acc,item)=>acc+item}
    }
  }

  test("test - obtenga el promedio de los numeros pares") {
    val lista = List(1, 2, 3, 4, 6, 7, 8, 9, 10)
    assert(true)
  }

  test("Una lista se debe poder dividir") {
    val lista = List(1, 2, 3, 4)
    val t: (List[Int], List[Int]) = lista.splitAt(2)
    assert(t._1 == List(1, 2) && t._2 == List(3, 4))
  }

  test("Una lista se debe poder reversar") {
    val lista = List(1, 2, 3, 4)
    assertResult(List(4, 3, 2, 1)) {
      lista.reverse
    }
  }

  test("Una lista se debe poder serializar") {
    val lista = List(1, 2, 3, 4)
    assertResult("1&2&3&4") {
      lista.mkString("&")
    }
  }

  // ----------------------------------

  test("Se debe poder acceder al primer elemento de List() de forma segura") {
    val lista = List()
    val result = lista.headOption
    assert(result == None)
  }


  test("Una List se debe poder transformar") {

    def f(s:String):String = s+"prueba"

    val lista = List("1", "2", "3")
    val lista2 = lista.map(dato => dato + "prueba")
    val lista3 = lista.map(dato => f(dato))

    assert(lista2.head == "1prueba")
    assert(lista != lista2)
    assert(lista2 == lista3)
  }

  test("Numero de caracteres sobre lista de String"){

    def a(s:String):Int = s.length

    val lista = List("ac", "abc", "abcd")
    val lista2 = lista.map(dato => a(dato))

    assert(lista2 == List(2,3,4))
  }

  test("Flatmap, una lista de listas y sacar el promedio de ello"){

    val lista = List(List(1,1),List(2,2), Nil)

    //Así sería con map
    //val result = lista.filter(!_.isEmpty).map(x => (x.sum/x.size))

    //flatMap siempre retorna una lista (aplanada)
    val result = lista.filter(!_.isEmpty).flatMap(x => List(x.sum/x.size))

    // 1) List(List(1,1), List(2,2))
    // 2) List(List(1), List(2))
    // 3) List(1,2)

    assert(result == List(1,2))

  }

  test("Promedio de números pares"){
    val lista = List(1, 2, 3, 4, 6, 7, 8, 9, 10)
    assert(6==lista.filter(_%2==0).fold(0)((a,e)=>a+e)/lista.filter(_%2==0).size)
  }

  /*test("Verificacion de map sobre una List"){
    case class MyCaseClass(nro:Int)
    val l = List(1, 2, 3)

    val r = l.map(numero => MyCaseClass(numero))

    assert(r == List(MyCaseClass(1),MyCaseClass(2),MyCaseClass(3)))

  }
*/

}

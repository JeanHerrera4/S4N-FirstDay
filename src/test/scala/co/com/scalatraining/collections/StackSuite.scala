package co.com.scalatraining.collections

import org.scalatest.FunSuite

import scala.collection.immutable.Stack

class StackSuite extends FunSuite {

  test("Crear pila vacía"){
    var ints:Stack[Int] = Stack()
    val ints2:Stack[Int] = Stack.empty
    assert(ints == ints2)
  }

  test("Adicción de un elemento a la pila"){
    val pila1 = Stack.empty[Int]
    val pila2 = pila1.push(3)
    assert(pila1 == Stack.empty[Int])
    assert(pila2 == Stack(3))
  }

  test("A una pila se le debe poder eliminar elementos con drop") {
    val pila = Stack(1, 2, 3, 4)
    val dropped =pila.drop(2)

    assertResult(Stack(3, 4)) {
      dropped
    }
  }

  test("A una pila se le pueden descartar elementos en dirección determinada (right)") {
    val pila = Stack(1, 2, 3, 4)
    assertResult(Stack(1, 2)) {
      pila.dropRight(2)
    }
  }

  test("Qué pasa si hacemos top a una pila y pila vacía()") {
    val pila = Stack(2,4,6)
    assertResult(2){
      pila.top
    }
    val pila2 = Stack()
    assertThrows[NoSuchElementException] {
      pila2.top
    }
  }

  test("Se debe poder obtener todos los elementos de una pila sin el primer elemento") {
    val pila = Stack(1, 2, 3, 4)
    assertResult(Stack(2, 3, 4)) {
      pila.tail
    }
  }

  test("Que pasa si hacemos tail a un Stack()") {
    val pila = Stack()
    assertThrows[UnsupportedOperationException] {
      val res =  pila.tail
    }
  }

  test("head en una stack") {
    val pila = Stack(1, 2, 3, 4)
    assertResult(1) {
      pila.head
    }
  }

  test("Borrar elementos con pop"){
    val pila = Stack[Int](1,2,3,4)
    assertResult(2,3,4){
      pila.pop
    }
  }




}